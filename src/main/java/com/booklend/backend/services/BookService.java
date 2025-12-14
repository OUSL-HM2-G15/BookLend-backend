package com.booklend.backend.services;

import com.booklend.backend.models.Book;
import com.booklend.backend.models.Category;
import com.booklend.backend.models.Location;
import com.booklend.backend.repositories.BookRepository;
import com.booklend.backend.repositories.CategoryRepository;
import com.booklend.backend.repositories.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository; 

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Post a new book
    public Book addBook(String title, String author, String description, Long categoryId,
                        BigDecimal feePerWeek, String status, Long locationId,
                        String isbn, Integer publishedYear, String image_url) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // Create book
        Book book = new Book();
        book.setUserId(1L); // temp — real user id later
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setCategory(category);
        book.setFeePerWeek(feePerWeek);
        book.setStatus(status);
        book.setAvailableLocation(location);
        book.setIsbn(isbn);
        book.setPublishedYear(publishedYear);
        book.setImageUrl(image_url); 

        return bookRepository.save(book); // Save and return the new book
    }

}
