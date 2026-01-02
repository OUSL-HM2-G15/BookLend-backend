package com.booklend.backend.repositories;
import com.booklend.backend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import com.booklend.backend.models.User;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
        List<Book> findByUser(User user);
}
