package com.booklend.backend.repositories;

import com.booklend.backend.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for accessing Location data in the database.
 * Extends JpaRepository for CRUD operations.
 */
@Repository
public interface  LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationName(String name);
    Optional<Location> findByLocationNameIgnoreCase(String name);
}


