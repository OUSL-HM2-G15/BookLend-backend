// Response DTO (lending book info) for nested book information in LendedBookDetailsDto

package com.booklend.backend.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LendedBookInfoDTO {

    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal feePerWeek;
    private String imageUrl;

}