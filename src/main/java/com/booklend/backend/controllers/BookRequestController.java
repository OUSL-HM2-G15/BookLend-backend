package com.booklend.backend.controllers;

import com.booklend.backend.models.BookRequest;
import com.booklend.backend.services.BookRequestService;
import com.booklend.backend.dto.NewBookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * REST Controller for handling Book Requests.
 * Provides endpoints for posting new requests and fetching user requests.
 */
@RestController
@RequestMapping("/api/book-requests")
@CrossOrigin(origins = "${frontend.url}")
public class BookRequestController {

    @Autowired
    private BookRequestService bookRequestService;

    // Add Logger for error handling
    private static final Logger logger = LoggerFactory.getLogger(BookRequestController.class);

    /**
     * POST /api/book-requests
     * Create a new book request.
     */
    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestBody NewBookDTO dto,
            Authentication authentication) {
        try {
            // Create the request and associate it with the logged-in user
            BookRequest request = bookRequestService.createRequest(
                    dto.getTitle(),
                    dto.getAuthor(),
                    dto.getLocationId(),
                    dto.getStatus(),
                    authentication);
            return ResponseEntity.ok(request); // Return created request
        } catch (RuntimeException e) {
            // Log the error details
            logger.error("Error creating book request: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage()); // Handle errors
        }
    }

    /**
     * GET /api/book-requests/me
     * Get all book requests posted by the logged-in user.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyRequests(Authentication authentication) {
        try {
            // Get the user's book requests using the service
            List<BookRequest> requests = bookRequestService.getMyRequests(authentication);
            return ResponseEntity.ok(requests); // Return the list of requests
        } catch (RuntimeException e) {
            // Log the error details
            logger.error("Error fetching book requests: " + e.getMessage(), e);
            return ResponseEntity.status(401).body(e.getMessage()); // Handle unauthorized access
        }
    }

    /**
     * DELETE /api/book-requests/{requestId}
     * Cancel a pending request
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> cancelRequest(@PathVariable int requestId, Authentication authentication) {
        try {
            bookRequestService.cancelRequest(requestId, authentication);
            return ResponseEntity.ok("Request cancelled successfully");

        } catch (RuntimeException e) {
            logger.error("Error cancelling request: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PUT /api/book-requests/{requestId}/rerequest
     * Re-request a cancelled request
     */
    @PutMapping("/{requestId}/rerequest")
    public ResponseEntity<?> rerequest(@PathVariable int requestId, Authentication authentication) {
        try {
            bookRequestService.reRequest(requestId, authentication);
            return ResponseEntity.ok("Request re-submitted successfully");

        } catch (RuntimeException e) {
            logger.error("Error re-requesting: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/book-requests/history
     * Fetch full request history (Pending, Cancelled, Available)
     */
    @GetMapping("/history")
    public ResponseEntity<?> getRequestHistory(Authentication authentication) {
        try {
            List<BookRequest> history = bookRequestService.getRequestHistory(authentication);
            return ResponseEntity.ok(history);

        } catch (RuntimeException e) {
            logger.error("Error fetching request history: " + e.getMessage(), e);
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PutMapping("/{requestId}/mark-as-available")
    public ResponseEntity<?> markAsAvailable(@PathVariable int requestId) {
        try {
            bookRequestService.markAsAvailable(requestId); // Call the existing service method
            return ResponseEntity.ok("Request marked as available now.");
        } catch (RuntimeException e) {
            logger.error("Error marking request as available now: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to mark request as available.");
        }
    }
}
