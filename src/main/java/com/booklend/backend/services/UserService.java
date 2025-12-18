package com.booklend.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booklend.backend.models.User;
import com.booklend.backend.models.Account;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.repositories.LocationRepository;
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
    private LocationRepository locationRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean checkIfUsernameExists(String username) {
        return accountRepository.existsByUsername(username);  // Use the method from AccountRepository
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

        // Check if location exists
        /*
        Optional<Location> location = locationRepository.findByLocationName(user.getLocation().getLocationName());
        if (location.isEmpty())
            return "Invalid location";
        user.setLocation(location.get());
        */
       var location = locationRepository.findByLocationName(user.getLocation().getLocationName())
                .orElseThrow(() -> new RuntimeException("Invalid location"));
        user.setLocation(location);
        

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

    /*
    ** Method for updating profile later (trim inputs)
    public User updateUserProfile(User user) {
        if (user.getEmail() != null) user.setEmail(user.getEmail().trim());
        if (user.getContactNo() != null) user.setContactNo(user.getContactNo().trim());
        if (user.getWhatsappNo() != null) user.setWhatsappNo(user.getWhatsappNo().trim());

        return userRepository.save(user);
    }
     */

}
