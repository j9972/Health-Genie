package com.example.healthgenie.global.config;

import com.example.healthgenie.global.config.auth.SecurityFilter;
import com.example.healthgenie.global.utils.SecurityUtils;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final JwtUtil jwtTokenProvider;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .cors()
                .and()
                .sessionManagement()//세션 정책 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/login/**", "/user", "/oauth2/**", "/auth/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/code/*")
        ;

        http.addFilterBefore(new SecurityFilter(securityUtils, userRepository), UsernamePasswordAuthenticationFilter.class);

//        http.csrf().disable();
//        http.httpBasic().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/login/**","/oauth2/**", "/auth/**").permitAll()
//                .requestMatchers("/**").hasAnyRole("ADMIN","USER")
//                .anyRequest().authenticated();
//
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.exceptionHandling()
//                .accessDeniedHandler(jwtAccessDeniedHandler);
//        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        return http.build();
    }
}
