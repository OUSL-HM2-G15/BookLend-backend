package com.booklend.backend.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogoutService {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    // Add token to blacklist until its expiration
    public void blacklist(String token, long expiryTime) {
        blacklistedTokens.put(token, expiryTime);
    }

    // Check if token is blacklisted
    public boolean isBlacklisted(String token) {
        // Log an error or warning that a null token was passed
        if (token == null)
            return false; // Null check
        Long expiry = blacklistedTokens.get(token);
        if (expiry == null)
            return false;

        if (expiry < System.currentTimeMillis()) {
            blacklistedTokens.remove(token); // cleanup expired token
            return false;
        }
        return true;
    }
}
