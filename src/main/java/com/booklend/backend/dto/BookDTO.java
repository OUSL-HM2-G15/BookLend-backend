package com.booklend.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String locationName;
    private Long locationId;
    private BigDecimal feePerWeek;
    private String status;   // Available / Unavailable
    private String imageUrl;
    private String isbn;
    private Integer publishedYear;
    private String description;
    private String categoryName;
    private Long categoryId;
    private LocalDateTime createdAt;
}

