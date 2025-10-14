package com.evolting.authservice.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {
    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    public JwtService() {
//        secretKey = generateSecretKey();
//    }
//
//    public String generateSecretKey() {
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGen.generateKey();
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error generating secret key", e);
//        }
//    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        Set<String> roles = userDetails.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> authority.getAuthority().substring(5))
                .collect(Collectors.toSet());
        log.info("Roles: " + roles);
        claims.put("roles", roles);

        Set<String> permissions = userDetails.getAuthorities().stream()
                .filter(authority -> !authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> authority.toString())
                .collect(Collectors.toSet());
        log.info("Permissions: " + permissions);
        claims.put("permissions", permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .setIssuer("auth-service")
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Duration getRemainingExpirationTime(String token) {
        Date expiration = extractExpiration(token);
        long remainingMillis = expiration.getTime() - System.currentTimeMillis();
        return Duration.ofMillis(Math.max(0, remainingMillis));
    }

    public void invalidateToken(String token) {
        Duration remainingTime = getRemainingExpirationTime(token);

        if (!remainingTime.isZero() && !remainingTime.isNegative()) {
            redisTemplate.opsForValue().set(
                    BLACKLIST_KEY_PREFIX + token,
                    "invalidated", // Value can be anything, key presence matters
                    remainingTime
            );
            log.info("JWT blacklisted: {}", token);
        } else {
            log.warn("Attempted to blacklist an already expired token.");
        }
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean isBlacklisted = redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + token);
        return isBlacklisted != null && isBlacklisted;
    }
}
