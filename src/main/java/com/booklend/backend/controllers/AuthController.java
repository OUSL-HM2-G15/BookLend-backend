package com.booklend.backend.controllers;

import com.booklend.backend.models.User;
import com.booklend.backend.services.AuthService;
import com.booklend.backend.services.UserService;
import com.booklend.backend.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication-related endpoints.
 * Handles registration and login requests.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // React frontend URL
public class AuthController {
    @Autowired
    private UserService userService; // Service for managing user data (e.g., registration)

    @Autowired
    private AuthService authService; // Service for handling authentication (e.g., login, JWT)

    /**
     * Registers a new user.
     * Expects a JSON body with user and account details.
     * Example:
     * {
     * "user": {...},
     * "account": {"username": "john123", "password": "StrongP@ss1", "role": "user"}
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        // Validate location
        if (request.getLocation() == null || request.getLocation().getLocationName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Location is required");
        }

        // Check password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Prepend +94 to numbers
        if (request.getContactNumber() != null && !request.getContactNumber().isEmpty()) {
            user.setContactNo("+94" + request.getContactNumber());
        }
        if (request.getWhatsappNumber() != null && !request.getWhatsappNumber().isEmpty()) {
            user.setWhatsappNo("+94" + request.getWhatsappNumber());
        }

        user.setLocation(request.getLocation());

        try {
            String result = userService.registerUser(user, request.getUsername(), request.getPassword(), "user");
            switch (result) {
                case "User registered successfully":
                    return ResponseEntity.ok(result);
                case "Username already exists":
                case "Email already exists":
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /**
     * Handles login request by validating credentials and returning JWT token if
     * valid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Call AuthService to validate credentials and generate JWT token
            String token = authService.loginUser(request.getUsername(), request.getPassword());
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Invalid
                                                                                                            // credentials
            }
            return ResponseEntity.ok(new JwtResponse(token)); // JWT token
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }

    }

    /**
     * DTO class to receive registration request from frontend
     */
    static class RegisterRequest {
        @jakarta.validation.constraints.NotBlank
        private String fullName;
        @jakarta.validation.constraints.NotBlank
        private String username;
        @jakarta.validation.constraints.NotBlank
        private String password;
        @jakarta.validation.constraints.NotBlank
        private String confirmPassword;
        @jakarta.validation.constraints.NotBlank
        private String email;
        private String contactNumber;
        private String whatsappNumber;
        private Location location;

        // Getters and Setters
        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getWhatsappNumber() {
            return whatsappNumber;
        }

        public void setWhatsappNumber(String whatsappNumber) {
            this.whatsappNumber = whatsappNumber;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    // LoginRequest DTO
    static class LoginRequest {
        @jakarta.validation.constraints.NotBlank
        private String username;
        @jakarta.validation.constraints.NotBlank
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // JWT response DTO
    static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
