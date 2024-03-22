package com.example.healthgenie.base.utils;

import com.example.healthgenie.base.constant.Constants;
import com.example.healthgenie.base.exception.Jwt.JwtErrorResult;
import com.example.healthgenie.base.exception.Jwt.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${custom.jwt.secret-key}")
    private String SECRET_KEY;
    private SecretKey key;

    @PostConstruct
    protected void init() {
        String secretKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String resolveToken(String header) {
        return header.replace("Bearer ", "");
    }

    public String generateAccessToken(String email, String role) {
        JwtBuilder builder = Jwts.builder()
                .subject(email)
                .claim("role", role);

        Date now = new Date();

        return builder
                .signWith(key)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + Constants.ACCESS_TOKEN_EXPIRE_COUNT))
                .compact();
    }

    public String generateRefreshToken(String email, String role) {
        JwtBuilder builder = Jwts.builder()
                .subject(email)
                .claim("role", role);

        Date now = new Date();

        return builder
                .signWith(key)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + Constants.REFRESH_TOKEN_EXPIRE_COUNT))
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isExpired(String token) {
        return new Date(Long.parseLong(decodeToken(token).get("exp")) * 1000).before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException(JwtErrorResult.WRONG_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new JwtException(JwtErrorResult.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(JwtErrorResult.UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new JwtException(JwtErrorResult.WRONG_TOKEN);
        }
    }

    public Authentication getAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Map<String, String> decodeToken(String token) {
        String[] split = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(split[1]));
        payload = payload.replaceAll("[{}\"]", "");

        Map<String, String> map = new HashMap<>();

        String[] contents = payload.split(",");
        for (String content : contents) {
            String[] c = content.split(":");
            map.put(c[0], c[1]);
        }

        return map;
    }
}