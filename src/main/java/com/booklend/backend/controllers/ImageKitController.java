package com.booklend.backend.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.imagekit.sdk.ImageKit;

@RestController
@RequestMapping("/api/imagekit")
public class ImageKitController {

    // Defining the expiration window in SECONDS - for 59 minutes
    private static final long EXPIRATION_WINDOW_SECONDS = 59 * 60;

    @GetMapping("/auth")
    public Map<String, String> getAuthParams() {        // Map to hold authentication parameters
        // Get the current time in seconds since the epoch
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // Convert milliseconds to seconds

        // making expiration time suitable for the token (current time + 59 minutes)
        long expireSeconds = currentTimeSeconds + EXPIRATION_WINDOW_SECONDS; 

        // Generate authentication parameters using the calculated time
        // Passing the calculated 'expireSeconds' to the SDK
        Map<String, String> auth = ImageKit.getInstance().getAuthenticationParameters( // auth params taken from ImageKit SDK
            null, // No need token for upload - token is for client-side uploads - private key is used on server-side
            expireSeconds // The calculated expire time in SECONDS
        );

        return auth; // Return token, expire time, and signature
    }
}