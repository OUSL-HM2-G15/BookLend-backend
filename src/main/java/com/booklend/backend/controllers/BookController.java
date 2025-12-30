package com.booklend.backend.controllers;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.booklend.backend.dto.BookDTO;
import com.booklend.backend.dto.BookRequestDTO;
import com.booklend.backend.models.Book;
import com.booklend.backend.services.BookService;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController // This class handles HTTP requests and returns JSON
@RequestMapping("/api/books") // Base URL for all APIs in this controller
@CrossOrigin(origins = "${frontend.url}") // Allow requests from React app
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * POST /api/books
     * Create a new book for the logged-in user
     */
   @PostMapping
   public ResponseEntity<?> addBook(@RequestBody Map<String, Object> params, Authentication authentication) {
    try {
        Book savedBook = bookService.addBook(
            authentication, // Pass authentication to service
            params.get("title").toString(),
            params.get("author").toString(),
            params.get("description").toString(),
            Long.parseLong(params.get("category_id").toString()),
            new BigDecimal(params.get("fee_per_week").toString()),
            params.get("status").toString(),
            Long.parseLong(params.get("available_location_id").toString()),
            params.get("isbn").toString(),
            Integer.parseInt(params.get("published_year").toString()),
            params.get("image_url").toString() //  get URL of uploaded image
        );
        return ResponseEntity.ok(savedBook); // Return the saved book as response

        } catch (Exception e) {
            log.error("exception message : {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage()); // Return error message with 500 status
        }
    }

    // getting book by id
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId) { // get 
        try {
            // Call the service layer to fetch the book by ID
            Book book = bookService.getBookById(bookId);
            if (book == null) {
               return ResponseEntity.status(404).body("Book not found"); // Message in response body
            }
            return ResponseEntity.ok(book);

        } catch (Exception e) {
            log.error("exception message : {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * GET /api/books/me
     * Returns books posted by the logged-in user
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyBooks(Authentication authentication) {
        try {
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }

            String username = authentication.getName();
            List<BookDTO> books = bookService.getMyBooks(username);

            return ResponseEntity.ok(books);

        } catch (RuntimeException e) {
            log.error("Error fetching user books: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while fetching books", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    /**
     * PUT /api/books/{bookId}/status
     * Update the status of a book
     */
    @PutMapping("/{bookId}/status")
    public ResponseEntity<?> updateBookStatus(
            @PathVariable Long bookId,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        try {
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }

            String status = body.get("status");
            if (status == null) {
                return ResponseEntity.badRequest().body("Status is required");
            }

            String username = authentication.getName();
            Book updatedBook = bookService.updateBookStatus(bookId, status, username);

            return ResponseEntity.ok(updatedBook);

        } catch (RuntimeException e) {
            log.error("Error updating book status: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }    
    }

    /**
     * PUT /api/books/me/{bookId}
     * Update book details
     */    
    @PutMapping("/me/{bookId}")
    public ResponseEntity<BookDTO> updateMyBook(
            @PathVariable Long bookId,
            @RequestBody BookRequestDTO bookRequestDTO,
            Authentication authentication
    ) {
            String username = authentication.getName();
            BookDTO updatedBook = bookService.updateMyBook(bookId, bookRequestDTO, username);

            return ResponseEntity.ok(updatedBook);
    }
}