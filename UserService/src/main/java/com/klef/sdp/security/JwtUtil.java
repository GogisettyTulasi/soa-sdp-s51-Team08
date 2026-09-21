package com.klef.sdp.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil
{
    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long expiration;

    // Generate JWT
    public String generateToken(UserDetails userDetails)
    {
        String role = userDetails
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + expiration))
                .id(UUID.randomUUID().toString())
                .signWith(getSigningKey())
                .compact();
    }

    // Extract username/email
    public String extractUsername(String token)
    {
        return getClaims(token).getSubject();
    }

    // Validate token
    public boolean validateToken(
            String token,
            UserDetails userDetails)
    {
        String username =
                extractUsername(token);

        return username.equals(
                    userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Check token expiration
    private boolean isTokenExpired(String token)
    {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // Get claims
    private Claims getClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .clockSkewSeconds(30)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Create signing key
    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes());
    }
}