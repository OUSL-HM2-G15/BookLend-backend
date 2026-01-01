package com.booklend.backend.services;

import com.booklend.backend.dto.BookDTO;
import com.booklend.backend.dto.BookRequestDTO;
import com.booklend.backend.models.Account;
import com.booklend.backend.models.Book;
import com.booklend.backend.models.Category;
import com.booklend.backend.models.Location;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.repositories.BookRepository;
import com.booklend.backend.repositories.CategoryRepository;
import com.booklend.backend.repositories.LocationRepository;
import com.booklend.backend.models.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository; 

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Post a new book
    public Book addBook(Authentication authentication, String title, String author, String description, Long categoryId,
                        BigDecimal feePerWeek, String status, Long locationId,
                        String isbn, Integer publishedYear, String image_url) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();  // Get logged-in user from JWT
        Account account = accountRepository.findById(username) // fetch account by username
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User currentUser = account.getUser();

        // Create book
        Book book = new Book();
        book.setUser(currentUser); // owner of the book
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

    // Get book by ID
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    /**
     * Get books posted by the currently logged-in user
     */
    public List<BookDTO> getMyBooks(String username) {

        Account account = accountRepository.findById(username) // fetch account by username
                .orElseThrow(() -> new RuntimeException("Account not found"));

        User user = account.getUser(); // get user associated with account

        List<Book> books = bookRepository.findByUser(user);

        return books.stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Maps Book entity to BookDTO
    private BookDTO mapToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setFeePerWeek(book.getFeePerWeek());
        dto.setStatus(book.getStatus());
        dto.setImageUrl(book.getImageUrl());
        dto.setLocationName(book.getAvailableLocation().getLocationName());
        dto.setLocationId(book.getAvailableLocation().getLocationId());
        dto.setIsbn(book.getIsbn());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setDescription(book.getDescription());
        dto.setCategoryName(book.getCategory().getCategoryName());
        dto.setCategoryId(book.getCategory().getCategoryId());
        dto.setCreatedAt(book.getCreatedAt());

        return dto;
    }

    // Reusable method to validate book ownership
    private void validateBookOwnership(Book book, User currentUser) {
        if (book.getUser().getUserId() != currentUser.getUserId()) {
            throw new RuntimeException("You are not allowed to modify this book");
        }
    }

    /**
     * Update the status of a book
     */
    public Book updateBookStatus(
            Long bookId,
            String status,
            String username
    ) {

        Account account = accountRepository.findById(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User currentUser = account.getUser();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        validateBookOwnership(book, currentUser);

        if (!status.equals("Available") && !status.equals("Unavailable")) {
            throw new RuntimeException("Invalid status value");
        }

        book.setStatus(status);
        return bookRepository.save(book);
    }

    /**
     * Update book details
     */ 
    public BookDTO updateMyBook(
            Long bookId,
            BookRequestDTO dto,
            String username
    ) {

        Account account = accountRepository.findById(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User currentUser = account.getUser();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        validateBookOwnership(book, currentUser);

        // Update book details on the book entity
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setFeePerWeek(dto.getFeePerWeek());
        book.setAvailableLocation(locationRepository.findById(dto.getLocationId())
            .orElseThrow(() -> new RuntimeException("Location not found")));
        book.setCategory(categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found")));
        book.setStatus(dto.getStatus());
        book.setImageUrl(dto.getImageUrl());
        book.setIsbn(dto.getIsbn());
        book.setPublishedYear(dto.getPublishedYear());
        book.setDescription(dto.getDescription());

        Book updatedBook = bookRepository.save(book);

        return mapToDto(updatedBook);    
    }
    // Maps Book entity to BookDTO
    private BookDTO mapToDto(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setFeePerWeek(book.getFeePerWeek());
        dto.setStatus(book.getStatus());
        dto.setImageUrl(book.getImageUrl());
        dto.setLocationName(book.getAvailableLocation().getLocationName());
        dto.setLocationId(book.getAvailableLocation().getLocationId());
        dto.setIsbn(book.getIsbn());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setDescription(book.getDescription());
        dto.setCategoryName(book.getCategory().getCategoryName());
        dto.setCategoryId(book.getCategory().getCategoryId());
        dto.setCreatedAt(book.getCreatedAt());

        return dto;
    }

    /**
     * Get a single book of logged-in user
     */
    public BookDTO getMyBookById(Long id, String username) {
        Account account = accountRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        User currentUser = account.getUser();

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        validateBookOwnership(book, currentUser);

        return mapToDto(book);
    }

    /**
     * delete logged-in user's book
     */
    public void deleteMyBook(Long id, String username) {

    Account account = accountRepository.findById(username)
            .orElseThrow(() -> new RuntimeException("Account not found"));

    User currentUser = account.getUser();

    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));

    validateBookOwnership(book, currentUser);

    bookRepository.delete(book);
    }

}