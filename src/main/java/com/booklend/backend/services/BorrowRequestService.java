package com.booklend.backend.services;

import com.booklend.backend.dto.BorrowedBookDTO;
import com.booklend.backend.models.*;
import com.booklend.backend.repositories.BorrowRequestRepository;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.repositories.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * Service for managing borrow requests.
 * Provides methods to fetch borrow requests for the logged-in user.
 */

@Service
public class BorrowRequestService {

    @Autowired
    private BorrowRequestRepository borrowRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BookRepository bookRepository;

    /** Create a new borrow request */
    public BorrowedBookDTO createRequest(Long bookId, Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        User borrower = account.getUser();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getUser().getUserId() == borrower.getUserId()) {
            throw new RuntimeException("Cannot borrow your own book");
        }

        // Allow multiple PENDING, block only ACCEPTED
        boolean alreadyBorrowed = borrowRequestRepository.existsByBook_BookIdAndStatus(
                        bookId,
                        BorrowStatus.Accepted
                ); 

        if (alreadyBorrowed) {
            throw new RuntimeException("This book is already borrowed");
        }

        // Block duplicate ACTIVE request by same user
        boolean duplicateActiveRequest =
                borrowRequestRepository.existsByBook_BookIdAndBorrower_UserIdAndStatusIn(
                        bookId,
                        borrower.getUserId(),
                        List.of(BorrowStatus.Pending, BorrowStatus.Accepted)
                );

        if (duplicateActiveRequest) {
            throw new RuntimeException("You already have an active request for this book");
        }

        BorrowRequest request = new BorrowRequest();
        request.setBook(book);
        request.setBorrower(borrower);
        request.setOwner(book.getUser());
        request.setStatus(BorrowStatus.Pending);
        request.setRequestedDate(LocalDateTime.now());

        borrowRequestRepository.save(request);

        return mapToDTO(request);
    }

    /**
     * Fetch all borrow requests for the currently logged-in user.
     * Maps BorrowRequest entities to BorrowedBookDTOs for frontend display.
     *
     * @param authentication Spring Security authentication object
     * @return List of BorrowedBookDTOs representing borrow requests
     */
    public List<BorrowedBookDTO> getMyBorrowedBooks(Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        int borrowerId = account.getUser().getUserId();
        // Fetch borrow requests with all relevant statuses (Pending, Accepted,
        // Rejected, and Returned)
        return borrowRequestRepository
                .findByBorrower_UserIdAndStatusIn(borrowerId,
                        List.of(BorrowStatus.Pending, BorrowStatus.Accepted, BorrowStatus.Rejected,
                                BorrowStatus.Returned))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /** Cancel a pending request */
    public void cancelRequest(Long requestId, Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        BorrowRequest request = borrowRequestRepository.findById(requestId.intValue())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getBorrower().getUserId() != account.getUser().getUserId()) {
            throw new RuntimeException("Not allowed");
        }

        if (request.getStatus() != BorrowStatus.Pending) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }

        // Update status to Cancelled instead of ignoring
        request.setStatus(BorrowStatus.Cancelled);
        request.setReturnedDate(LocalDateTime.now()); // Mark time of cancellation

        borrowRequestRepository.save(request); // save updated status
    }

    /** Close a request (e.g., returned or rejected) */
    public void closeRequest(Integer requestId, Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        BorrowRequest request = borrowRequestRepository.findById(requestId.intValue())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getBorrower().getUserId() != account.getUser().getUserId()) {
            throw new RuntimeException("Not allowed");
        }

        if (!(request.getStatus() == BorrowStatus.Rejected || request.getStatus() == BorrowStatus.Returned)) {
            throw new RuntimeException("This request cannot be closed");
        }
        borrowRequestRepository.save(request);
    }

    /** Re reguest book that are cancelled */
    public void reRequest(Long requestId, Authentication authentication) {
        Account account = accountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        BorrowRequest request = borrowRequestRepository.findById(requestId.intValue())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getBorrower().getUserId() != account.getUser().getUserId()) {
            throw new RuntimeException("You cannot re-request this book");
        }

        if (request.getStatus() != BorrowStatus.Cancelled) {
            throw new RuntimeException("Only cancelled requests can be re-requested");
        }

        // Prevent re-request if book already borrowed by someone else
        boolean alreadyBorrowed =
                borrowRequestRepository.existsByBook_BookIdAndStatus(
                        request.getBook().getBookId(),
                        BorrowStatus.Accepted
                );

        if (alreadyBorrowed) {
            throw new RuntimeException("This book is already borrowed");
        }

        // Change status back to Pending for re-request
        // Reuse same request, do NOT create new one 
        request.setStatus(BorrowStatus.Pending); 
        borrowRequestRepository.save(request);
    }

    /**
     * Helper method to convert BorrowRequest entity to BorrowedBookDTO.
     *
     * @param borrowRequest BorrowRequest entity
     * @return BorrowedBookDTO with relevant data for frontend
     */
    public BorrowedBookDTO mapToDTO(BorrowRequest borrowRequest) {
        BorrowedBookDTO dto = new BorrowedBookDTO();

        // Mapping basic fields
        dto.setRequestId((long) borrowRequest.getRequestId());
        dto.setBookId((long) borrowRequest.getBook().getBookId());
        dto.setTitle(borrowRequest.getBook().getTitle());
        dto.setAuthor(borrowRequest.getBook().getAuthor());
        dto.setFeePerWeek(borrowRequest.getBook().getFeePerWeek());
        dto.setStatus(borrowRequest.getStatus().name()); // Enum to String
        dto.setImageUrl(borrowRequest.getBook().getImageUrl());

        // Location mapping
        dto.setLocationName(borrowRequest.getBook().getAvailableLocation() != null
                ? borrowRequest.getBook().getAvailableLocation().getLocationName() : null);

        // Date mappings
        dto.setRequestedDate(borrowRequest.getRequestedDate());
        dto.setAcceptedDate(borrowRequest.getAcceptedDate());
        dto.setReturnedDate(borrowRequest.getReturnedDate());

        // Owner info mapping
        if (borrowRequest.getOwner() != null) {
            BorrowedBookDTO.OwnerInfo ownerInfo = new BorrowedBookDTO.OwnerInfo();
            ownerInfo.setName(borrowRequest.getOwner().getFullName());
            ownerInfo.setPhone(borrowRequest.getOwner().getContactNo());
            ownerInfo.setWhatsappNumber(borrowRequest.getOwner().getWhatsappNo());
            dto.setOwner(ownerInfo);
        }

        return dto;
    }
}
