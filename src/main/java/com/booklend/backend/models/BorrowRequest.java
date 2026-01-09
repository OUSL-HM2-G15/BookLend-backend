package com.booklend.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a borrow request.
 * Maps directly to `borrow_requests` table in DB.
 * Contains references to the book, borrower, and owner.
**/

@Getter
@Setter
@Entity
@Table(name = "borrow_requests")
public class BorrowRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;  // Pending / Accepted / Rejected / Returned / Cancelled

    private LocalDateTime requestedDate;
    private LocalDateTime acceptedDate;
    private LocalDateTime returnedDate;

}
