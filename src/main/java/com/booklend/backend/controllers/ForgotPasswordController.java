package com.booklend.backend.controllers;

import com.booklend.backend.services.AuthService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import com.booklend.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling password reset functionalities
 * such as sending a reset password link to the user's email.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${frontend.url}")

public class ForgotPasswordController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService; // Email service to send reset link

    /**
     * Endpoint to handle the "forgot password" request.
     * It generates a password reset token and sends it to the user's email.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        String responseMessage = authService.generateResetToken(email);

        if ("Invalid email".equals(responseMessage)) {
            return ResponseEntity.status(400).body("Email is not registered");
        }

        emailService.sendResetPasswordEmail(email, responseMessage);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }
}