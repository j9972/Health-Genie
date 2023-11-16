//package com.example.healthgenie.global.config.auth;
//
//
//import com.example.healthgenie.global.config.JwtUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//@Log4j2
//public class JwtTokenFilter extends OncePerRequestFilter {
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String REFRESH_HEADER = "Refresh";
//
//    private final JwtUtil jwtTokenProvider;
//
//    public JwtTokenFilter(JwtUtil jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String jwt = resolveToken(request, AUTHORIZATION_HEADER);
//
//        if (jwt != null && jwtTokenProvider.validateToken(jwt) == JwtUtil.JwtCode.ACCESS) {
//            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI());
//        }
//
//        else if( jwt != null && jwtTokenProvider.validateToken(jwt) == JwtUtil.JwtCode.EXPIRED){
//            String refresh = resolveToken(request, REFRESH_HEADER);
//
//            // refresh token을 확인해서 재발급해준다
//            if(refresh != null && jwtTokenProvider.validateToken(refresh) == JwtUtil.JwtCode.ACCESS){
//
//                //기존 refresh토큰의 role을 얻어온다 함수작성
//                Map<String,String> map = jwtTokenProvider.getEmailAndRole(refresh);
//
//                String email = map.get("email");
//                String role = map.get("role");
//                String newRefresh = jwtTokenProvider.reissueRefreshToken(refresh,role,email);
//
//                log.info(role+" "+email);
//                if(newRefresh != null){
//                    response.setHeader(REFRESH_HEADER, newRefresh);
//
//                    // access token 생성을 위해 authentication 얻기
//                    Authentication authentication = jwtTokenProvider.getAuthentication(refresh);
//
//                    // access token 생성을 위해 email,role 알아야됨
//                    response.setHeader(AUTHORIZATION_HEADER, jwtTokenProvider.createAccessToken(email,role));
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));
//
//                    Authentication token = new UsernamePasswordAuthenticationToken(email,null,authorities);
//                    log.info(token.getAuthorities()+" "+token.getName());
//
//                    SecurityContextHolder.getContext().setAuthentication(token);
//                    log.info("reissue refresh Token & access Token");
//                }
//            }
//        }
//        else {
//            log.info("no valid JWT token found, uri: {}, auth :{}", request.getRequestURI(),SecurityContextHolder.getContext().getAuthentication());
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String resolveToken(HttpServletRequest request, String header) {
//        String bearerToken = request.getHeader(header);
//        if (bearerToken != null) {
//            return bearerToken;
//        }
//        return null;
//    }
//}
