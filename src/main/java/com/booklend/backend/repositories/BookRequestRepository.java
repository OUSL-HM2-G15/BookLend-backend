package com.booklend.backend.repositories;

import com.booklend.backend.models.BookRequest;
import com.booklend.backend.models.Location;
import com.booklend.backend.models.RequestStatus;
import com.booklend.backend.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface to access book_requests table in DB.
 */
@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Integer> {

    List<BookRequest> findByLocation(Location location);

    List<BookRequest> findByUser(User user);

    List<BookRequest> findByUserAndStatus(User user, RequestStatus status);

   //  Fetch multiple statuses for active requests
    List<BookRequest> findByUserAndStatusIn(User user, List<RequestStatus> statuses);
}