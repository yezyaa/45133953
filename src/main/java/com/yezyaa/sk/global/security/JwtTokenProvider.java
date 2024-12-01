package com.yezyaa.sk.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // AccessToken 생성
    public String createAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId)); // 사용자 ID 저장
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime); // 만료 시간 설정

        return Jwts.builder()
                .setClaims(claims) // Payload 설정
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }
}
