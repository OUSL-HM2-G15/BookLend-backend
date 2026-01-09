// Response DTO for lended books
// List view → My Lended Books page -- Feeds: LendedBookCard
// Used by: GET /api/lended-books - for listing lended books

package com.booklend.backend.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.booklend.backend.models.BorrowStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LendedBookDto {
    
    int requestId;

    // Book info
    long bookId;
    String title;
    String author;
    String imageUrl;
    BigDecimal feePerWeek;

    // Borrower info
    String borrowerName;
    String borrowerLocation;

    // Status & dates
    BorrowStatus status;
    LocalDateTime requestDate;
    LocalDateTime acceptedDate;
    LocalDateTime returnedDate;

    // UI state flags
    boolean acceptDisabled;
    String disableReason;

    // Constructor needed for JPQL projection
    public LendedBookDto(
    int requestId,
    long bookId,
    String title,
    String author,
    String imageUrl,
    BigDecimal feePerWeek,
    String borrowerName,
    String borrowerLocation,
    BorrowStatus status,
    LocalDateTime requestDate,
    LocalDateTime acceptedDate,
    LocalDateTime returnedDate  
) {
    this.requestId = requestId;
    this.bookId = bookId;
    this.title = title;
    this.author = author;
    this.imageUrl = imageUrl;
    this.feePerWeek = feePerWeek;
    this.borrowerName = borrowerName;
    this.borrowerLocation = borrowerLocation;
    this.status = status;
    this.requestDate = requestDate;
    this.acceptedDate = acceptedDate;
    this.returnedDate = returnedDate;

    // UI defaults
    this.acceptDisabled = false;
    this.disableReason = null;
 }
}
