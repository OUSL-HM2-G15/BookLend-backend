package com.booklend.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * User entity class that represents the "users" table in the database.
 * The users table stores information about each user, including their location, contact info, etc.
 */

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    /**
     * Primary Key (PK) for the User table. 
     * Auto-incremented by MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    /**
     * Full name of the user. 
     * This field cannot be null as per database constraint.
     */
    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * Profile picture of the user (URL or file path).
     * This is optional; it can be NULL.
     */
    @Column(name = "profile_pic")
    private String profilePic;

    /**
     * Contact number of the user (e.g., mobile number).
     * Stored as a string to allow for different formats.
     */
    @Size(min = 10, max = 15, message = "Contact number must be between 10 and 15 characters")
    @Column(name = "contact_no",nullable = false)
    private String contactNo;  // stored as +94712345678

    /**
     * WhatsApp number of the user.
     * Stored as a string for flexibility in format.
     */
    @Size(min = 10, max = 15, message = "WhatsApp number must be between 10 and 15 characters")
    @Column(name = "whatsapp_no")
    private String whatsappNo; // same stored as +94712345678

    /**
     * Email address of the user.
     * Stored as a string.
     */
    @Email(message = "Please provide a valid email address")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Foreign Key (FK) referencing the Location table.
     * Represents the location of the user, like  Colombo, Kandy, etc.
     */
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private Location location;

    /**
     * Date and time when the user account was created.
     * Default value is the current timestamp.
     */
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Default constructor required by JPA (Hibernate)
    public User() {}

    // Optional we can override toString() for better logging and debugging
    @Override
    public String toString() {
        return "User{id=" + userId + ", name='" + fullName + "', email='" + email + "'}";
    }
}