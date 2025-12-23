package com.booklend.backend.dto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO to send JWT token back to the client after successful login.
 */
@Getter
@Setter

public class JwtResponse {

    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }
}
