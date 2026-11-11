package com.healthcare.patient.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final SecretKey signingKey;

    public JwtUtil(
            @Value("${security.jwt.secret}") String secret) {

        byte[] keyBytes = Decoders.BASE64URL.decode(secret);

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract email
    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }

    // Extract user ID
    public Object extractUserId(String token) {

        return extractAllClaims(token).get("userId");
    }

    // Extract role
    public String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);
    }

    // Validate token
    public boolean validateToken(String token) {

        try {
            extractAllClaims(token);
            return true;

        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}