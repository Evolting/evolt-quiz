package dev.evolting.questionservice.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer:auth-service}")
    private String expectedIssuer;

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .requireIssuer(expectedIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }

    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = validateToken(token);
        return claims.get("roles", List.class);
    }

    public List<String> extractPermissions(String token) {
        Claims claims = validateToken(token);
        return claims.get("permissions", List.class);
    }
}
