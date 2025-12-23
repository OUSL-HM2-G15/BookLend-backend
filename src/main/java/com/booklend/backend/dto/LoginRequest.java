package com.booklend.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class to receive login request from frontend
 * Contains username and password for authentication
 */
@Getter
@Setter

public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
