package com.booklend.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booklend.backend.models.User;

/**
 * UserRepository interface extends JpaRepository to provide CRUD operations for User entities.
 * It allows access to the database for retrieving, saving, updating, and deleting User records.
 */

public interface UserRepository extends JpaRepository<User, Integer> {
     /**
      * UserRepository extends JpaRepository which automatically provides methods for CRUD (Create, Read, Update, Delete) operations for the User model.
      * The Integer is the type of the primary key (ID) of the User model.
      * We can add custom query methods here later if needed, like finding a user by email
      */
     Optional<User> findByEmail(String email);
}
