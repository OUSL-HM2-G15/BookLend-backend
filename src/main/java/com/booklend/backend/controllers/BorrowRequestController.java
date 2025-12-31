package com.booklend.backend.controllers;

import com.booklend.backend.dto.BorrowedBookDTO;
import com.booklend.backend.services.BorrowRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling borrow requests.
 * Provides endpoint to fetch borrowed books for the logged-in user.
 */
@RestController
@RequestMapping("/api/borrow-requests")
@CrossOrigin(origins = "${frontend.url}")

public class BorrowRequestController {

    @Autowired
    private BorrowRequestService borrowRequestService;

    /**
     * POST /api/borrow-requests
     * Create a new borrow request (from Explore page button)
     */
    @PostMapping
    public ResponseEntity<?> createBorrowRequest(@RequestParam Long bookId, Authentication authentication) {
        try {
            BorrowedBookDTO dto = borrowRequestService.createRequest(bookId, authentication);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/borrow-requests/me
     * Returns all borrowed books for the currently logged-in user.
     *
     * @param authentication Spring Security authentication object
     * @return List of BorrowedBookDTOs
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyBorrowedBooks(Authentication authentication) {
        try {
            List<BorrowedBookDTO> borrowedBooks = borrowRequestService.getMyBorrowedBooks(authentication);
            return ResponseEntity.ok(borrowedBooks);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    /**
     * DELETE /api/borrow-requests/{requestId}
     * Cancel borrow request (only Pending)
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> cancelBorrowRequest(
            @PathVariable Long requestId,
            Authentication authentication) {
        try {
            borrowRequestService.cancelRequest(requestId, authentication);
            return ResponseEntity.ok("Borrow request canceled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    /**
     * PUT /api/borrow-requests/{requestId}/close
     * Close borrow request (mark as Returned)
     */
    @PutMapping("/{requestId}/close")
    public ResponseEntity<?> closeBorrowRequest(
            @PathVariable Long requestId,
            Authentication authentication) {
        try {
            borrowRequestService.closeRequest(requestId, authentication);
            return ResponseEntity.ok("Borrow request closed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

}
