package com.harshmithaiwala.expensetracking.expensetracking.config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class); // ✅ Add Logger

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Generate JWT token
     */
    public String generateToken(String email) {
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        logger.info("✅ JwtUtil: Generated token for email {}: {}", email, token);
        return token;
    }

    /**
     * Extract email from JWT token
     */
    public String extractEmail(String token) {
        try {
            String email = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            logger.info("✅ JwtUtil: Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("🚫 JwtUtil: Failed to extract email from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            logger.info("✅ JwtUtil: Token is valid.");
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("🚫 JwtUtil: Token is expired!");
        } catch (MalformedJwtException e) {
            logger.warn("🚫 JwtUtil: Token is malformed!");
        } catch (SignatureException e) {
            logger.warn("🚫 JwtUtil: Token signature is invalid!");
        } catch (Exception e) {
            logger.error("🚫 JwtUtil: Token validation failed: {}", e.getMessage());
        }
        return false;
    }
}
