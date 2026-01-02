package com.booklend.backend.dto;

import com.booklend.backend.models.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class to receive registration request from frontend
 * Contains all user and account details required for registration
 */
@Getter
@Setter

public class RegisterRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String email;

    private String contactNumber;

    private String whatsappNumber;

    private Location location;

}
