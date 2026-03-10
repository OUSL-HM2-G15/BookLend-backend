package com.booklend.backend.controllers;

import com.booklend.backend.models.BookRequest;
import com.booklend.backend.services.BookRequestService;
import com.booklend.backend.dto.NewBookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * REST Controller for handling Book Requests.
 * Provides endpoints for posting new requests and fetching user requests.
 */
@RestController
@RequestMapping("/api/book-requests")
@CrossOrigin(origins = "${frontend.url}")
@Slf4j
public class BookRequestController {

    @Autowired
    private BookRequestService bookRequestService;

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
            log.error("Error creating book request: " + e.getMessage(), e);
            return ResponseEntity.status(401).body(e.getMessage()); // Handle errors
        }
    }

    /**
     * GET /api/book-requests/user
     * Get all book requests posted by the logged-in user.
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserRequests(Authentication authentication) {
        try {
            // Get the user's book requests using the service
            List<BookRequest> requests = bookRequestService.getUserRequests(authentication);
            return ResponseEntity.ok(requests); // Return the list of requests
        } catch (RuntimeException e) {
            // Log the error details
            log.error("Error fetching book requests: " + e.getMessage(), e);
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
            log.error("Error cancelling request: " + e.getMessage(), e);
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
            log.error("Error re-requesting: " + e.getMessage(), e);
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
            log.error("Error fetching request history: " + e.getMessage(), e);
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PutMapping("/{requestId}/mark-as-available")
    public ResponseEntity<?> markAsAvailable(@PathVariable int requestId, Authentication authentication) {
        try {
            bookRequestService.markAsAvailable(requestId, authentication);
            return ResponseEntity.ok("Request marked as available now.");
        } catch (RuntimeException e) {
            log.error("Error marking request as available now: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to mark request as available.");
        }
    }

    /**
     * GET /api/book-requests/received
     * Get book requests received for the logged-in user
     * (location-based matching)
     */
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedRequests(Authentication authentication) {
        try {
            return ResponseEntity.ok(
                    bookRequestService.getRequestsReceivedForUser(authentication));
        } catch (RuntimeException e) {
            log.error("Error fetching received requests: " + e.getMessage(), e);
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    /**
     * GET /api/book-requests/{requestId}
     * Fetch a single request for autofill
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable int requestId, Authentication authentication) {
        try {
            BookRequest request = bookRequestService.getRequestById(requestId, authentication);
            return ResponseEntity.ok(request);
        } catch (RuntimeException e) {
            log.error("Error fetching request by ID: {}", e.getMessage());
            int status = e.getMessage().contains("Unauthorized") ? 401
                    : e.getMessage().contains("Forbidden") ? 403 : 404;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

}
