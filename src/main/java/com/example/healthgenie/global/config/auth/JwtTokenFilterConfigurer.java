//package com.example.healthgenie.global.config.auth;
//
//import com.example.healthgenie.global.config.JwtUtil;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private final JwtUtil jwtTokenProvider;
//
//    public JwtTokenFilterConfigurer(JwtUtil jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    public void configure(HttpSecurity builder) throws Exception {
//        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
//        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
