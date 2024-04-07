package com.example.healthgenie.base.config;

import com.example.healthgenie.base.filter.CustomLogoutFilter;
import com.example.healthgenie.base.filter.JwtExceptionFilter;
import com.example.healthgenie.base.filter.JwtFilter;
import com.example.healthgenie.base.handler.CustomFailureHandler;
import com.example.healthgenie.base.handler.CustomSuccessHandler;
import com.example.healthgenie.boundedContext.auth.service.CustomOAuth2UserService;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final JwtFilter jwtFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomLogoutFilter customLogoutFilter;

    private final String[] COMMON_WHITE_LIST = new String[]
            {
                    "/login/**", "/oauth2/**", "/refresh", "/error/**", "/ws/**", "/favicon.ico/**",
                    "/routine/genie/**", "/routine/genie/detail/**", "/auth/mail/**"
            };
    private final String[] GET_WHITE_LIST = new String[]
            {
                    "/community/posts/**", "/trainer/profile/detail/**"
            };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .addFilterBefore(customLogoutFilter, LogoutFilter.class);

        http
                .oauth2Login(
                        config -> config
                                .redirectionEndpoint(
                                        redirectionConfig -> redirectionConfig.baseUri("/oauth2/code/*")
                                )
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