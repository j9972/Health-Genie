package com.example.healthgenie.global.config;

import com.example.healthgenie.global.constants.constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtil {

//    public JwtUtil(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret) {
//        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
//        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
//    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }
    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
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
        return createToken(claims, email, constant.ACCESS_TOKEN_EXPIRE_COUNT);
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(String email, String role) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email, constant.REFRESH_TOKEN_EXPIRE_COUNT);
    }

    // signWith(key,SignatureAlgorithm) 에서 key는 string 이 아니라 byte 형태로 받아야 한다
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(constant.SECRET_KEY);
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
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
