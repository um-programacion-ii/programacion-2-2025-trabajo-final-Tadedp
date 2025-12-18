package com.example.proxy.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String stringKey;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public String generarToken(String username, Long usuarioId) {
        Key key = Keys.hmacShaKeyFor(stringKey.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .claim("usuarioId", usuarioId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }
}