package com.booklend.backend.services;

import com.booklend.backend.models.Account;
import com.booklend.backend.models.BorrowRequest;
import com.booklend.backend.models.User;
import com.booklend.backend.repositories.BorrowRequestRepository;
import com.booklend.backend.repositories.BookRepository;
import com.booklend.backend.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booklend.backend.dto.BorrowerDto;
import com.booklend.backend.dto.LendedBookDetailsDto;
import com.booklend.backend.dto.LendedBookInfoDTO;
import com.booklend.backend.dto.LendedBookDto;
import com.booklend.backend.models.BorrowStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LendedBookService {

    private final BorrowRequestRepository borrowRequestRepo;
    private final BookRepository bookRepo;
    private final AccountRepository accountRepository;

    public LendedBookService(BorrowRequestRepository borrowRequestRepo,
                             BookRepository bookRepo,
                             AccountRepository accountRepository) {
        this.borrowRequestRepo = borrowRequestRepo;
        this.bookRepo = bookRepo;
        this.accountRepository = accountRepository;
    }

    // ---------------- Get list of My lended books ----------------
    public List<LendedBookDto> getMyLendedBooks(String username) {
        // Get the owner (User) from Account
        Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User owner = account.getUser();
        if (owner == null) 
            throw new RuntimeException("User not found");
    
        int ownerId = owner.getUserId();

        // Get all LendedBook DTOs
        List<LendedBookDto> lendedBooks = borrowRequestRepo.findLendedBooksByOwner(ownerId);

        // Get all books that already have an accepted request
        List<Long> acceptedBookIds = borrowRequestRepo.findAcceptedBookIdsByOwner(ownerId, BorrowStatus.Accepted);

        // Set UI flags for each DTO
        for (LendedBookDto dto : lendedBooks) { // Iterate through each lended book DTO
            boolean bookHasAcceptedRequest = acceptedBookIds.contains(dto.getBookId()); // Check if this book has an accepted request

            // Set UI flags
            dto.setAcceptDisabled(bookHasAcceptedRequest && dto.getStatus() == BorrowStatus.Pending);
            dto.setDisableReason(bookHasAcceptedRequest ? "This book is already borrowed by another user." : null); // null if not disabled
        }
            return lendedBooks;
    }

    // ---------------- Get details of a single borrow request ----------------
    public LendedBookDetailsDto getDetails(int requestId, String username) {
        Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User owner = account.getUser();
        if (owner == null) {
            throw new RuntimeException("User not found");
    }
        BorrowRequest br = borrowRequestRepo.findByRequestIdAndOwner_UserId(requestId, owner.getUserId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Map Book Info
        LendedBookInfoDTO bookDto = new LendedBookInfoDTO();
        bookDto.setBookId(br.getBook().getBookId());
        bookDto.setTitle(br.getBook().getTitle());
        bookDto.setAuthor(br.getBook().getAuthor());
        bookDto.setFeePerWeek(br.getBook().getFeePerWeek());
        bookDto.setImageUrl(br.getBook().getImageUrl());

        // Map Borrower Info
        BorrowerDto borrowerDto = new BorrowerDto();
        borrowerDto.setId(br.getBorrower().getUserId());
        borrowerDto.setFullName(br.getBorrower().getFullName());
        borrowerDto.setEmail(br.getBorrower().getEmail());
        borrowerDto.setContactNo(br.getBorrower().getContactNo());
        borrowerDto.setLocation(br.getBorrower().getLocation() != null ? br.getBorrower().getLocation().getLocationName() : null);

        // Build final DTO
        LendedBookDetailsDto dto = new LendedBookDetailsDto();
        dto.setRequestId(br.getRequestId());
        dto.setStatus(br.getStatus().name());
        dto.setRequestDate(br.getRequestedDate());
        dto.setAcceptedDate(br.getAcceptedDate());
        dto.setReturnedDate(br.getReturnedDate());
        dto.setBook(bookDto);
        dto.setBorrower(borrowerDto);

        return dto;
    }

    // ---------------- Accept a borrow request ----------------
    public void acceptRequest(int requestId, String username) {
        Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User owner = account.getUser();
        if (owner == null) {
            throw new RuntimeException("User not found");
        }

        BorrowRequest br = borrowRequestRepo.findByRequestIdAndOwner_UserId(requestId, owner.getUserId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!br.getStatus().equals(BorrowStatus.Pending)) {
            throw new RuntimeException("Request is not pending");
        }
        
        long bookId = br.getBook().getBookId();

        // Check for existing accepted requests for the same book
        boolean alreadyAccepted = borrowRequestRepo
                .existsByBook_BookIdAndStatus(bookId, BorrowStatus.Accepted);

        if (alreadyAccepted) {
            throw new RuntimeException(
                "You have already accepted this borrow request for another user"
            );
        }
        
        // Update borrow request status
        br.setStatus(BorrowStatus.Accepted);
        br.setAcceptedDate(LocalDateTime.now());

        // Update book status
        br.getBook().setStatus("Unavailable");

        // Save entities
        borrowRequestRepo.save(br);
        bookRepo.save(br.getBook());
    }

    // ---------------- Reject a borrow request ----------------
    public void rejectRequest(int requestId, String username) {
        Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User owner = account.getUser();
        if (owner == null) {
            throw new RuntimeException("User not found");
        }
        BorrowRequest br = borrowRequestRepo.findByRequestIdAndOwner_UserId(requestId, owner.getUserId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!br.getStatus().equals(BorrowStatus.Pending)) {
            throw new RuntimeException("Request is not pending");
        }

        br.setStatus(BorrowStatus.Rejected);
        borrowRequestRepo.save(br);
    }

    // ---------------- Mark as returned ----------------
    public void markReturned(int requestId, String username) {
        Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        User owner = account.getUser();
        if (owner == null) {
            throw new RuntimeException("User not found");
        }
        BorrowRequest br = borrowRequestRepo.findByRequestIdAndOwner_UserId(requestId, owner.getUserId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!br.getStatus().equals(BorrowStatus.Accepted)) {
            throw new RuntimeException("Request is not accepted");
        }

        br.setStatus(BorrowStatus.Returned);
        br.setReturnedDate(LocalDateTime.now());
        br.getBook().setStatus("Available");

        borrowRequestRepo.save(br);
        bookRepo.save(br.getBook());
        }
}
