package com.example.healthgenie.base.config;

import com.example.healthgenie.base.filter.CustomLogoutFilter;
import com.example.healthgenie.base.filter.JwtExceptionFilter;
import com.example.healthgenie.base.filter.JwtFilter;
import com.example.healthgenie.base.handler.CustomSuccessHandler;
import com.example.healthgenie.boundedContext.auth.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtFilter jwtFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomLogoutFilter customLogoutFilter;

    private final String[] COMMON_WHITE_LIST = new String[]
            {
                    "/login/**", "/oauth2/**", "/token", "/refresh", "/error/**", "/ws/**", "/favicon.ico/**",
                    "/routine/genie/**", "/routine/genie/detail/**", "/auth/mail/**"
            };
    private final String[] GET_WHITE_LIST = new String[]
            {
                    "/community/posts/**", "/trainer/profile/detail/**"
            };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.httpBasic(AbstractHttpConfigurer::disable);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class);

        http.addFilterBefore(customLogoutFilter, LogoutFilter.class);

        http.oauth2Login(
                (oauth2) -> oauth2
                        .userInfoEndpoint(
                                (userInfoEndpointConfig -> userInfoEndpointConfig
                                        .userService(customOAuth2UserService))
                        )
                        .successHandler(customSuccessHandler)
                );

        http
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers(COMMON_WHITE_LIST).permitAll()
                                .requestMatchers(HttpMethod.GET, GET_WHITE_LIST).permitAll()
                                .anyRequest().authenticated()
                );

        http
                .sessionManagement(
                        (session) -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}