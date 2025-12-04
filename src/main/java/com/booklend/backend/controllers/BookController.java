package com.booklend.backend.controllers;
import java.util.List;
import com.booklend.backend.models.Book;  
import com.booklend.backend.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // This class handles HTTP requests and returns JSON
@RequestMapping("/api/books") // Base URL for all APIs in this controller
@CrossOrigin(origins = "${frontend.url}") // Allow requests from React app
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
}