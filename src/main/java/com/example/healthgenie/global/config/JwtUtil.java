package com.example.healthgenie.global.config;

import com.example.healthgenie.domain.user.entity.RefreshToken;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {

    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerUsersDetailsService customerUsersDetailsService;
    private Key key;


    @Override
    public void afterPropertiesSet() throws Exception {
        key = getSignInKey();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        // Jwts.parser().setSigningKey(secret) 대신 Jwts.parserBuilder().setSigningKey(secret).build()
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    // 만료 기간 체크
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String email, String role) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email);
    }

    /**
     * AccessToken 생성
     */
    public String createAccessToken(String email, String role) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
//        return createToken(claims, email, constant.ACCESS_TOKEN_EXPIRE_COUNT);
        return createToken(claims, email, 1 * 60 * 1000L);

    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(String email, String role) {
        Map<String,Object> claims = new ConcurrentHashMap<>();
        claims.put("role", role);
        return createToken(claims, email, 7 * 24 * 60 * 60 * 1000L);
//        return createToken(claims, email, constant.REFRESH_TOKEN_EXPIRE_COUNT);

    }

    // signWith(key,SignatureAlgorithm) 에서 key는 string 이 아니라 byte 형태로 받아야 한다
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode("4D635166546A576E5A7234753778214125442A462D4A614E645267556B5870324D635166546A576E5A7234753778214125442A462D4A614E645267556B587032");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // (role 을 적용해서) token 을 생성하려고 함
    private String createToken(Map<String,Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // (role 을 적용해서) token 을 생성하려고 함
    private String createToken(Map<String,Object> claims, String subject,Long expire) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // token의 유효성을 검사하는 로직 ( 데이터 정보는 맞나? 기간이 만료되지는 않았나? )
    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e){
            // 만료된 경우에는 refresh token을 확인하기 위해
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("jwtException : {}", e.getMessage());
        }
        return JwtCode.DENIED;
    }
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        UserDetails userDetails = customerUsersDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public Role getRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String roleCode = customerUsersDetailsService.loadUserByRole(claims.getSubject());
        Role role = null;
        if(roleCode.equals(Role.GUEST.getCode()) ){
            role =  Role.GUEST;
        }
        else if(roleCode.equals(Role.USER.getCode())){
            role =  Role.USER;
        }
        return role;
    }


    public Map<String,String> getEmailAndRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Map<String,String> map = new ConcurrentHashMap<>();
        map.put("email",claims.getSubject());
        map.put("role",claims.get("role").toString());
        return map;
    }

    @Transactional
    public String reissueRefreshToken(String refreshToken,String role,String email) throws RuntimeException{
        // refresh token을 디비의 그것과 비교해보기
        Authentication authentication = getAuthentication(refreshToken);
        log.info("getname "+authentication.getName());
        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("userId : " + authentication.getName() + " was not found"));
        if(findRefreshToken.getToken().equals(refreshToken)){
            // 새로운거 생성
            String newRefreshToken = createRefreshToken(email,role);
            findRefreshToken.changeToken(newRefreshToken);
            return newRefreshToken;
        }
        else {
            log.info(findRefreshToken.getToken());
            log.info(refreshToken);
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }

    public static enum JwtCode{
        DENIED,
        ACCESS,
        EXPIRED
    }
}

