package com.booklend.backend.repositories;
import com.booklend.backend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {
    
}
