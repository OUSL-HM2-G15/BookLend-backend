package com.booklend.backend.controllers;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.booklend.backend.dto.BookDTO;
import com.booklend.backend.models.Book;  
import com.booklend.backend.services.BookService;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
   public ResponseEntity<?> addBook(@RequestBody Map<String, Object> params,
                                    Authentication authentication
   ) {
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
    public ResponseEntity<List<BookDTO>> getMyBooks(Authentication authentication) {
        List<BookDTO> books = bookService.getMyBooks(authentication);
        return ResponseEntity.ok(books);
    }

}