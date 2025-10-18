package com.example.fnb.shared.security;

import com.example.fnb.shared.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UUID userId, UserRole role, String staffAtStore, int expiresInSeconds) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expiresInSeconds);

        return Jwts.builder()
            .subject(userId.toString())
            .claim("role", role.name())
            .claim("store", staffAtStore)
            .expiration(Date.from(expiresAt))
            .signWith(getSignKey())
            .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UUID extractUserId(String token) {
        String subject = Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
        return UUID.fromString(subject);
    }

    public UserRole extractRole(String token) {
        String roleClaim = Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
        return UserRole.valueOf(roleClaim);
    }

    public String extractStore(String token) {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("store", String.class);
    }

    public Instant extractExpiration(String token) {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .toInstant();
    }
}
