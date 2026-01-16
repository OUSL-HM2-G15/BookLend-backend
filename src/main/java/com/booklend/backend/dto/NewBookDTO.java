package com.booklend.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NewBookDTO {
    private String title;
    private String author;
    private Long locationId;
    private String status;
}