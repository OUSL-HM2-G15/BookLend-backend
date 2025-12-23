package com.booklend.backend.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Show login credentials for a user.
 * Maps to the 'account' table in the database.
 */

@Entity
@Table(name = "account") // Maps to 'account' table in the DB
@Getter
@Setter

public class Account {
    
    /**
     * Primary Key (PK) for the Account table. 
     * This is a unique username.
     */
    @Id
    @Column(name = "username")
    private String username;

    /**
     * Role of the user (e.g., admin, user).
     * The role is stored as a string with a limit of 10 characters.
     * Roles will help define the privileges for different users.
     */
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * Encrypted password of the user.
     * The password is stored as a string, which will be hashed using bcrypt or another secure hashing algorithm.
     */
    @JsonIgnore  // To avoid leaking hashed passwords.
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Foreign Key (FK) referencing the User table.
     * This links the account to a specific user.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // Default constructor required by JPA (Hibernate)
    public Account() {}

    // Utility method to hash password before saving
    public static String hashPassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);
    }

}
