package com.example.healthgenie.base.config;

import com.example.healthgenie.base.filter.JwtAuthenticationFilter;
import com.example.healthgenie.base.filter.JwtExceptionFilter;
import com.example.healthgenie.base.handler.JwtAccessDeniedHandler;
import com.example.healthgenie.base.handler.JwtAuthenticationEntryPoint;
import com.example.healthgenie.base.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    private final String[] COMMON_WHITE_LIST = new String[]
            {
                    "/login/**", "/oauth2/**", "/h2-console/**", "/refresh", "/error/**", "/ws/**",
                    "/routine/genie/**", "/routine/genie/detail/**", "/auth/mail/**",
            };
    private final String[] GET_WHITE_LIST = new String[]
            {
                    "/community/posts/**", "/trainer/profile/detail/**"
            };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers().frameOptions().disable()

                .and()
                .cors()

                .and()
                .sessionManagement()//세션 정책 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers(COMMON_WHITE_LIST).permitAll()
                .requestMatchers(HttpMethod.GET, GET_WHITE_LIST).permitAll()
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/code/*")
        ;

        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}