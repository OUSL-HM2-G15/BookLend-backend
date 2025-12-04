package com.booklend.backend.services;

import com.booklend.backend.models.Book;
import com.booklend.backend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository; 

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
