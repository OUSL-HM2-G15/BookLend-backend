package com.booklend.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entity representing a borrow request.
 * Maps directly to `borrow_requests` table in DB.
 * Contains references to the book, borrower, and owner.
 */

@Entity
@Table(name = "borrow_requests")
@Getter
@Setter

public class BorrowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "status")
    private String status; // Pending / Accepted / Rejected / Returned

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Column(name = "accepted_date")
    private LocalDateTime acceptedDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;
}
