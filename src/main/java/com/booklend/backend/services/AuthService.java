package com.booklend.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.booklend.backend.models.Account;
import com.booklend.backend.models.User;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.config.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Authenticates the user and generates a JWT token if the credentials are valid.
     */
    public String loginUser(String username, String password) {
        Optional<Account> accountOpt = accountRepository.findById(username);  // Find account by email
        if (accountOpt.isEmpty()) {
            return null;  // If account doesn't exist
        }
           
       Account account = accountOpt.get();
        if (!passwordEncoder.matches(password, account.getPassword())) {
            return null;  // password mismatch
        }

        return jwtUtil.generateToken(account);
    }

    // For password reset
    public String generateResetToken(String email) {
        Optional<User> userOpt = accountRepository.findAll().stream()
                .map(Account::getUser)
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
        if (userOpt.isEmpty()) return "Invalid email";

        // generate a UUID or JWT token as reset token
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Resets the password using the provided reset token.
     * (Note: Actual token validation is yet to be implemented)
     */
    public boolean resetPassword(String token, String newPassword) {
        // implement token validation & - TO DO
        return true;
    }
}