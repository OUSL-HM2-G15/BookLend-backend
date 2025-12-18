package com.booklend.backend.config;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.booklend.backend.models.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;


@Component
public class JwtUtil {
    // **Use environment variable instead of hardcoding**
    @Value("${jwt.secret}")
    private String secretKey; // Keep this key safe and secure

    @Value("${jwt.expiration.ms:3600000}") // Default 1 hour
    private long jwtExpirationMs;

     private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for the authenticated account.
     * 
     * @param account - The Account object containing user details
     * @return JWT token
     */
    public String generateToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getUsername()) // Use username as subject
                .claim("role", account.getRole()) // Attach user role to the token
                .setIssuedAt(new Date()) // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 1-hour expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with secret key
                .compact(); // Return the token
    }

    /**
     * Parses the JWT token to extract claims.
     * 
     * @param token - The JWT token to parse
     * @return Claims object (contains user details like username and role)
     */
    
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Use the secret key to validate the token
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Return claims
    }

    /**
     * Extracts the username (subject) from the token.
     * 
     * @param token - The JWT token
     * @return Username
     */
    public String extractUsername(String token) {
        return parseToken(token).getSubject(); // Extract username from token
    }

    
    /**
     * Validates the JWT token.
     * @param token - The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;  // Token is valid
        } catch (Exception e) {
            return false;  // Token is invalid
        }
    }
}
