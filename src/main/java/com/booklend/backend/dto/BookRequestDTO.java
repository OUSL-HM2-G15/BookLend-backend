package com.booklend.backend.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDTO  {
    private Long id;
    private String title;
    private String author;
    private Long locationId;
    private BigDecimal feePerWeek;
    private String status;
    private String imageUrl;
    private String isbn;
    private Integer publishedYear;
    private String description;
    private Long categoryId;
}
