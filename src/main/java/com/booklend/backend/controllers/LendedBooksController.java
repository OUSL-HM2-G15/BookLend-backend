package com.booklend.backend.controllers;

import com.booklend.backend.dto.LendedBookDto;
import com.booklend.backend.dto.LendedBookDetailsDto;
import com.booklend.backend.services.LendedBookService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lended-books")
@Slf4j
public class LendedBooksController {

    private final LendedBookService service;

    public LendedBooksController(LendedBookService service) {
        this.service = service;
    }

    // GET /api/lended-books
    @GetMapping
    public ResponseEntity<List<LendedBookDto>> getMyLendedBooks(Authentication authentication) {
        try {
            String username = authentication.getName();
            return ResponseEntity.ok(service.getMyLendedBooks(username));
        } catch (RuntimeException e) {
            log.error("Error fetching lended books: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    // GET /api/lended-books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<LendedBookDetailsDto> getDetails(@PathVariable int id, Authentication authentication) {
        try {
            String username = authentication.getName();
            return ResponseEntity.ok(service.getDetails(id, username));
        } catch (RuntimeException e) {
            log.error("Error fetching request details: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /api/lended-books/{id}/accept
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable int id, Authentication authentication) {
        try {
            String username = authentication.getName();
            service.acceptRequest(id, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error accepting request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/lended-books/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable int id, Authentication authentication) {
        try {
            String username = authentication.getName();
            service.rejectRequest(id, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error rejecting request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/lended-books/{id}/return
    @PutMapping("/{id}/return")
    public ResponseEntity<?> markReturned(@PathVariable int id, Authentication authentication) {
        try {
            String username = authentication.getName();
            service.markReturned(id, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error marking returned: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
