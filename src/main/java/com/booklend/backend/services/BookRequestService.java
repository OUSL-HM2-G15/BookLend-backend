package com.booklend.backend.services;

import com.booklend.backend.dto.BookRequestReceivedDTO;
import com.booklend.backend.models.BookRequest;
import com.booklend.backend.models.Location;
import com.booklend.backend.models.RequestStatus;
import com.booklend.backend.models.User;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.repositories.BookRequestRepository;
import com.booklend.backend.repositories.LocationRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class for handling book requests logic.
 */
@Slf4j
@Service
public class BookRequestService {

    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Create a new book request for the logged-in user.
     * 
     * @param string
     */
    public BookRequest createRequest(String title, String author, long locationId,
            String string, Authentication authentication) {

        try {
            // Store the user from authentication
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();

            // Ensure the location exists
            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Location not found"));

            BookRequest request = new BookRequest();
            request.setTitle(title);
            request.setAuthor(author);
            request.setUser(user);
            request.setLocation(location);
            request.setStatus(RequestStatus.Pending); // Always Pending
            request.setCreatedAt(LocalDateTime.now());
            return bookRequestRepository.save(request);
        } catch (RuntimeException e) {
            // Log the error details
            log.error("Error creating book request: {}", e.getMessage(), e);
            throw e; // Re-throw the exception to handle it in the controller
        }
    }

    /**
     * Get all book requests posted by the logged-in user.
     */
    public List<BookRequest> getUserRequests(Authentication authentication) {
        try {
            // Fetch the user based on authentication
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();

            // Fetch and return all the requests made by this user
            return bookRequestRepository.findByUser(user);
        } catch (RuntimeException e) {
            log.error("Error fetching book requests: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Fetch full history (Pending + Available + Cancelled)
    public List<BookRequest> getRequestHistory(Authentication authentication) {
        try {
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();
            return bookRequestRepository.findByUser(user);
        } catch (RuntimeException e) {
            log.error("Error fetching book request history: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Cancel a Pending request
     */
    public void cancelRequest(int requestId, Authentication authentication) {
        try {
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();

            BookRequest request = bookRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            if (!request.getUser().equals(user))
                throw new RuntimeException("Cannot cancel others' requests");
            if (request.getStatus() != RequestStatus.Pending)
                throw new RuntimeException("Only Pending requests can be cancelled");

            request.setStatus(RequestStatus.Cancelled);
            bookRequestRepository.save(request);
        } catch (RuntimeException e) {
            log.error("Error cancelling request: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Re-request a canceled request (this creates a new request with the same
     * details
     * but 'Pending' status)
     */
    public void reRequest(int requestId, Authentication authentication) {
        try {
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();

            BookRequest request = bookRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            if (!request.getUser().equals(user))
                throw new RuntimeException("Cannot re-request others' requests");
            if (request.getStatus() != RequestStatus.Cancelled)
                throw new RuntimeException("Only Cancelled requests can be re-requested");

            request.setStatus(RequestStatus.Pending);
            bookRequestRepository.save(request);
        } catch (RuntimeException e) {
            log.error("Error re-requesting request: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Mark request as fulfilled (This book is available now)
     * Used when another user posts a book to satisfy this request.
     */
    public void markAsAvailable(int requestId, Authentication authentication) {
        try {
            BookRequest request = bookRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            // Ensure current user is in same location
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();
                    
            if (!request.getLocation().getLocationId().equals(user.getLocation().getLocationId())) {
                throw new RuntimeException("Forbidden: cannot update request outside your location");
            }

            // Only mark the request as available if it's currently Pending
            if (request.getStatus() != RequestStatus.Pending)
                throw new RuntimeException("Only Pending requests can be marked as available");

            // Set the status to "This book is available now"
            request.setStatus(RequestStatus.This_book_is_available_now);
            bookRequestRepository.save(request);

        } catch (RuntimeException e) {
            log.error("Error marking request as available: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get book requests received for the logged-in user
     * (location-based matching)
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<BookRequestReceivedDTO> getRequestsReceivedForUser(Authentication authentication) {
        try {
            User user = accountRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getUser();

            if (user.getLocation() == null) {
                throw new RuntimeException("User location not set");
            }

            List<BookRequest> requests = bookRequestRepository.findByLocation_LocationIdAndUser_UserIdNotAndStatus(
                    user.getLocation().getLocationId(),
                    user.getUserId(),
                    RequestStatus.Pending);

            return requests.stream()
                    .map(br -> new BookRequestReceivedDTO(
                            br.getBookRequestId(),
                            br.getTitle(),
                            br.getAuthor(),
                            br.getLocation().getLocationName(),
                            br.getUser().getFullName(),
                            br.getStatus().name(),
                            br.getCreatedAt().format(DATE_FORMAT)))
                    .toList();

        } catch (RuntimeException e) {
            log.error("Error fetching received requests: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Fetch a single book request by its ID
     * Only visible to users from the same location
     */
    public BookRequest getRequestById(int requestId, Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        // Get logged-in user
        User currentUser = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUser();

        // Fetch request from repository
        BookRequest request = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Restrict visibility only to same-location users
        if (!request.getLocation().getLocationId().equals(currentUser.getLocation().getLocationId())) {
            throw new RuntimeException("Forbidden: Request not in your location");
        }

        return request;
    }

}
