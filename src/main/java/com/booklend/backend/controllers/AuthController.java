package com.booklend.backend.controllers;

import com.booklend.backend.models.User;
import com.booklend.backend.repositories.LocationRepository;
import com.booklend.backend.services.LogoutService;
import com.booklend.backend.services.AuthService;
import com.booklend.backend.services.UserService;
import com.booklend.backend.dto.RegisterRequest;
import com.booklend.backend.dto.LoginRequest;
import com.booklend.backend.config.JwtUtil;
import com.booklend.backend.dto.JwtResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.booklend.backend.models.Location;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for authentication-related endpoints.
 * Handles registration and login requests.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${frontend.url}") // React frontend URL
public class AuthController {
    @Autowired
    private UserService userService; // Service for managing user data (e.g., registration)

    @Autowired
    private AuthService authService; // Service for handling authentication (e.g., login, JWT)

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LogoutService logoutService;

    @Autowired
    private JwtUtil jwtUtil;

    // /**
    // * Helper method to format phone numbers to +94XXXXXXXXX
    // */
    // private String formatNumber(String number) {
    // if (number == null || number.isEmpty())
    // return null;

    // // Remove all non-digit characters
    // number = number.replaceAll("\\D", "");

    // if (number.length() == 10 && number.startsWith("0")) {
    // // Remove leading 0
    // number = number.substring(1);
    // } else if (number.length() != 9) {
    // // Invalid number length
    // return null;
    // }

    // return "+94" + number; // Correct format
    // }

    /**
     * Registers a new user.
     * Expects a JSON body with user and account details.
     * 
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {

        // Validate locationName
        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Location is required"));
        }

        Optional<Location> locationOpt = locationRepository.findByLocationName(request.getLocation());
        if (locationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid location"));
        }

        // Check password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Passwords do not match"));
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Format phone numbers
        // String contact = formatNumber(request.getContactNumber());
        // if (contact == null)
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid contact
        // number");
        // user.setContactNo(contact);

        // String whatsapp = formatNumber(request.getWhatsappNumber());
        // if (whatsapp == null)
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid WhatsApp
        // number");
        // user.setWhatsappNo(whatsapp);
        user.setContactNo(request.getContactNumber());
        user.setWhatsappNo(request.getWhatsappNumber());

        user.setLocation(locationOpt.get());

        try {
            String result = userService.registerUser(user, request.getUsername(), request.getPassword(), "user");
            switch (result) {
                case "User registered successfully":
                    return ResponseEntity.ok(Map.of("message", result));
                case "Username already exists":
                case "Email already exists":
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", result));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", result));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred"));
        }

    }

    /**
     * Handles login request by validating credentials and returning JWT token if
     * valid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
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
     * stateless JWT authentication with a one-hour expiration and an in-memory
     * blacklist to support immediate logout without database overhead.
     * Logged-out tokens are invalidated instantly and automatically removed after
     * expiration
     */

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            long expiryTime = jwtUtil.extractExpiration(token).getTime();
            logoutService.blacklist(token, expiryTime);
        }

        return ResponseEntity.ok("You are now logged out. See you soon!");
    }

}
