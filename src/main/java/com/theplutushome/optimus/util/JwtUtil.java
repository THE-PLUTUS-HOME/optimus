package com.theplutushome.optimus.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@PropertySource("classpath:application.properties")
@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final String SECRET;
    private final SecretKey SIGNING_KEY;

    @Autowired
    public JwtUtil (Environment env){
        SECRET = env.getProperty("jwt.secret.key");
        assert SECRET != null;
        SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username) {
        long EXPIRATION_TIME = 60_000 * 2;
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY)
                .compact();
    }

    public Claims extractClaim(String token) throws ExpiredJwtException {
        try {
            return Jwts.parser()
                    .verifyWith(SIGNING_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaim(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    public boolean tokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }

    public void verifyToken(String authHeader) {
        if (authHeader.isBlank()) {
            throw new JwtException("Missing authentication header");
        }

        String token = authHeader.substring(7);

        try {
            if (this.isTokenExpired(token)) {
                Map<String, Object> headerValues = new HashMap<>();
                headerValues.put("alg", "HS256"); // Algorithm
                headerValues.put("typ", "JWT");  // Token type

                Header header = new DefaultHeader(headerValues);
                Claims claims = this.extractClaim(token); // Ensure this method properly extracts claims
                throw new ExpiredJwtException(header, claims, "Token has expired");
            }
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT structure: " + ex.getMessage());
        } catch (SignatureException ex) {
            throw new JwtException("Invalid JWT signature: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new JwtException("Invalid or empty JWT: " + ex.getMessage());
        }
    }

}
