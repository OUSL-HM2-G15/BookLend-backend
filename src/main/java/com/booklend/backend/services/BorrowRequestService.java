package com.booklend.backend.services;

import com.booklend.backend.dto.BorrowedBookDTO;
import com.booklend.backend.models.BorrowRequest;
import com.booklend.backend.models.User;
import com.booklend.backend.models.Book;
import com.booklend.backend.models.Account;
import com.booklend.backend.repositories.BorrowRequestRepository;
import com.booklend.backend.repositories.AccountRepository;
import com.booklend.backend.repositories.BookRepository;

//import lombok.extern.slf4j.Slf4j;
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
        Account account = accountRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        User borrower = account.getUser();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getUser().getUserId() == borrower.getUserId()) {
            throw new RuntimeException("Cannot borrow your own book");
        }

        BorrowRequest request = new BorrowRequest();
        request.setBook(book);
        request.setBorrower(borrower);
        request.setOwner(book.getUser());
        request.setStatus("Pending");
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
        Account account = accountRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        int borrowerId = account.getUser().getUserId();

    return borrowRequestRepository.findByBorrower_UserId(borrowerId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    /** Cancel a pending request */
    public void cancelRequest(Long requestId, Authentication authentication) {
        Account account = accountRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        BorrowRequest request = borrowRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getBorrower().getUserId() != account.getUser().getUserId()) {
            throw new RuntimeException("Not allowed");
        }

        if (!"Pending".equals(request.getStatus())) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }

        request.setStatus("Cancelled");
        borrowRequestRepository.save(request);
    }


    /** Close a request (e.g., returned or rejected) */
    public void closeRequest(Long requestId, Authentication authentication) {
         Account account = accountRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        BorrowRequest request = borrowRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getBorrower().getUserId() != account.getUser().getUserId()) {
            throw new RuntimeException("Not allowed");
        }

        request.setStatus("Closed");
        borrowRequestRepository.save(request);
    }


    /**
     * Helper method to convert BorrowRequest entity to BorrowedBookDTO.
     *
     * @param borrowRequest BorrowRequest entity
     * @return BorrowedBookDTO with relevant data for frontend
     */
    private BorrowedBookDTO mapToDTO(BorrowRequest borrowRequest) {
        BorrowedBookDTO dto = new BorrowedBookDTO();
        dto.setRequestId(borrowRequest.getRequestId());
        dto.setBookId(borrowRequest.getBook().getBookId());
        dto.setTitle(borrowRequest.getBook().getTitle());
        dto.setAuthor(borrowRequest.getBook().getAuthor());
        dto.setFeePerWeek(borrowRequest.getBook().getFeePerWeek());
        dto.setStatus(borrowRequest.getStatus());
        dto.setImageUrl(borrowRequest.getBook().getImageUrl());
        dto.setLocationName(borrowRequest.getBook().getAvailableLocation().getLocationName());
        dto.setRequestedDate(borrowRequest.getRequestedDate());
        dto.setAcceptedDate(borrowRequest.getAcceptedDate());
        dto.setReturnedDate(borrowRequest.getReturnedDate());

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
