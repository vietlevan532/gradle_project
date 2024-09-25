package com.globaltechjsc.vanvietle.gradle_project.config.jwt;

import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final long TOKEN_VALIDITY_IN_SECONDS = 86400;
    private static final long TOKEN_VALIDITY_IN_SECONDS_REMEMBER_ME = 2592000;

    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public JwtService() {
        this.tokenValidityInMilliseconds = 1000 * TOKEN_VALIDITY_IN_SECONDS;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * TOKEN_VALIDITY_IN_SECONDS_REMEMBER_ME;
    }

    public String extractLogin(String token) {
        return extractAllClaims(token).get("loginBy", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user, String loginBy, boolean rememberMe) {
        Map<String, Object> claims = claimsMap(user, loginBy);
        return generateToken(claims, user, rememberMe);
    }

    @NotNull
    private Map<String, Object> claimsMap(User user, String loginBy) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("login_by", loginBy);
        claims.put("user_id", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhoneNumber());
        claims.put("status", user.getStatus());
        claims.put("role", user.getRole());
        return claims;
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            User user,
            boolean rememberMe
    ) {
        long validity = rememberMe ? this.tokenValidityInMillisecondsForRememberMe
                : this.tokenValidityInMilliseconds;
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String login = extractLogin(token);
        return (login.equals(user.getEmail()) ||
                login.equals(user.getUsername()) ||
                login.equals(user.getPhoneNumber())) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
