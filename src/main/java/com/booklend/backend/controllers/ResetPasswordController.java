package com.booklend.backend.controllers;

import com.booklend.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity; 

/**
 * Controller for handling password reset functionality.
 * It accepts a reset token and the new password from the user.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class ResetPasswordController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint to reset the password using a reset token and the new password.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        boolean isResetSuccessful = authService.resetPassword(token, password);

        if (isResetSuccessful) {
            return ResponseEntity.ok("Password successfully reset.");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired token.");
        }
    }
}
