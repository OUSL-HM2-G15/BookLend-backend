// Response DTO for Lended Book Details - nested Book(LandedBookInfoDTO.java) and Borrower info(BorrowerDto.java)
// Used by: GET /api/lended-books/{id}

package com.booklend.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class LendedBookDetailsDto {
    private int requestId;
    private String status;

    private LocalDateTime requestDate;
    private LocalDateTime acceptedDate;
    private LocalDateTime returnedDate;

    private LendedBookInfoDTO book; // nested book info
    private BorrowerDto borrower; // nested borrower info

}
