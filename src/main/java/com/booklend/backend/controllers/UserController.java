package com.booklend.backend.controllers;

import com.booklend.backend.dto.UserDataDTO;
import com.booklend.backend.models.Account;
import com.booklend.backend.models.User;
import com.booklend.backend.repositories.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * GET /api/users/me
     * Returns the profile of the currently logged-in user.
     */
@GetMapping("/me")
public ResponseEntity<?> getMyProfile(Authentication authentication) {
    try {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }

        String username = authentication.getName(); // extracted from JWT

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
        dto.setRole(account.getRole()); // frontend may need to know user role

        return ResponseEntity.ok(dto);

    } catch (RuntimeException e) {
        log.error("Profile fetch error: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());

    } catch (Exception e) {
        log.error("Unexpected error while fetching profile", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
    }
}

}
