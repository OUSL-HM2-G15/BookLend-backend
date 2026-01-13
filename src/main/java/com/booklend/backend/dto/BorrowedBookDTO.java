package com.booklend.backend.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a borrow request for a book.
 * This is used to send necessary data to the frontend for the BorrowedBooks page.
 */
@Getter
@Setter

public class BorrowedBookDTO {
    private Long requestId;         
    private Long bookId;             
    private String title;            
    private String author;           
    private String locationName;     
    private BigDecimal feePerWeek;   
    private String status;           // Borrow request status: Pending / Accepted / Rejected / Returned
    private String imageUrl;         // Book image url - imagekit
    private LocalDateTime requestedDate; // When the request was made
    private LocalDateTime acceptedDate;  // When the request was accepted 
    private LocalDateTime returnedDate;  // When the book was returned 

    // Owner info for tooltip in frontend and 
    // Also hidden book details
    private OwnerInfo owner;

    @Getter
    @Setter
    public static class OwnerInfo {
        private String name;
        private String phone;
        private String whatsappNumber;
    }
}
