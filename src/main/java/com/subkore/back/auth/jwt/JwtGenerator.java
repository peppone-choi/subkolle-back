package com.subkore.back.auth.jwt;

import com.subkore.back.user.dto.JwtDto;
import com.subkore.back.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
    private final Key key;

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtDto generateToken(User user) {
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + (60 * 30 * 1000));

        String accessToken = Jwts.builder()
            .setSubject(user.getEmail())
            .claim("auth", user.getRole())
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + (60 * 60 * 24 * 14 * 1000)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return JwtDto.builder()
            .accessToken("Bearer " + accessToken)
            .refreshToken("Bearer " + refreshToken)
            .build();
    }
}
