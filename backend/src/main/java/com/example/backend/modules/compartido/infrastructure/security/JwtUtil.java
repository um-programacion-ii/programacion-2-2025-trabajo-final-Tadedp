package com.example.backend.modules.compartido.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

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

    public boolean validarToken(String token, String username) {
        final String usernameExtraido = extraerUsername(token);
        return (usernameExtraido.equals(username) && !esTokenExpirado(token));
    }

    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public Long extraerUsuarioId(String token) {
        return extraerClaim(token, claims -> claims.get("usuarioId", Long.class));
    }

    private Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerClaims(String token) {
        Key key = Keys.hmacShaKeyFor(stringKey.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }
}