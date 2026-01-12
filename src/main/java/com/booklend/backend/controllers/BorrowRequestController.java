package com.booklend.backend.controllers;

import com.booklend.backend.dto.BorrowedBookDTO;
import com.booklend.backend.models.Account;
import com.booklend.backend.models.BorrowRequest;
import com.booklend.backend.services.BorrowRequestService;
import com.booklend.backend.repositories.BorrowRequestRepository;
import com.booklend.backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private BorrowRequestRepository borrowRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

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
            @PathVariable Integer requestId,
            Authentication authentication) {
        try {
            borrowRequestService.cancelRequest(requestId.longValue(), authentication);
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
            @PathVariable Integer requestId,
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

    @PutMapping("/{requestId}/re-request")
    public ResponseEntity<?> reRequest(@PathVariable Long requestId, Authentication authentication) {
        try {
            borrowRequestService.reRequest(requestId, authentication);
            return ResponseEntity.ok("Request re-submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    /**
     * GET /api/borrow-requests/history
     * Get the borrowing history for the currently logged-in user (including
     * cancelled, rejected, and returned requests).
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(Authentication authentication) {
        try {
            // Get the account associated with the logged-in user
            Account account = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Get the userId from the Account's associated User entity
            int borrowerId = account.getUser().getUserId();

            // Use the borrowerId to find all the borrow requests (including cancelled,
            // rejected, etc.)
            List<BorrowRequest> requests = borrowRequestRepository.findByBorrower_UserId(borrowerId);

            // Map BorrowRequest entities to BorrowedBookDTOs
            List<BorrowedBookDTO> dtos = requests.stream()
                    .map(borrowRequestService::mapToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the borrow history.");
        }
    }
}
