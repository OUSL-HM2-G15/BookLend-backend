package com.booklend.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booklend.backend.models.User;
import com.booklend.backend.dto.UserDataDTO;
import com.booklend.backend.models.Account;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.repositories.UserRepository;

/**
 * Service layer handles user registration and business logic.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean checkIfUsernameExists(String username) {
        return accountRepository.existsByUsername(username); // Use the method from AccountRepository
    }

    /**
     * Registers a new user with hashed password.
     */
    @Transactional
    public String registerUser(User user, String username, String rawPassword, String role) {
        // Avoid accidental spaces breaking uniqueness
        username = username.trim();
        rawPassword = rawPassword.trim();
        user.setEmail(user.getEmail().trim());
        user.setFullName(user.getFullName().trim());

        // Check if username already exists

        if (accountRepository.existsByUsername(username)) {
            return "Username already exists";
        }

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists";
        }

        // Hash password using BCrypt
        String hashedPassword = passwordEncoder.encode(rawPassword);

        userRepository.save(user);

        // Create Account
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(hashedPassword);
        account.setRole(role);
        account.setUser(user);
        accountRepository.save(account);

        return "User registered successfully";
    }

    // Fetch user data by username

    public UserDataDTO getMyProfile(String username) {

        Account account = accountRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = account.getUser();

        UserDataDTO dto = new UserDataDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setContactNumber(user.getContactNo());
        dto.setWhatsappNumber(user.getWhatsappNo());
        dto.setProfilePic(user.getProfilePic());
        dto.setLocationName(user.getLocation().getLocationName());
        dto.setUsername(account.getUsername());
        dto.setRole(account.getRole());

        return dto;
    }

}
