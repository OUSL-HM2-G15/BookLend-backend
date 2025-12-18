package com.booklend.backend.repositories;

import com.booklend.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for accessing Account data in the database.
 * Extends JpaRepository for CRUD operations.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUsername(String username);  // Custom query to check if account exists by username   
    Optional<Account> findByUsername(String username);
}

    
