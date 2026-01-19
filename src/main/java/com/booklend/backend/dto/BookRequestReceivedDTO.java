package com.booklend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookRequestReceivedDTO {

    private int requestId;
    private String title;
    private String author;
    private String locationName;
    private String requesterName;
    private String status;
    private String createdAt;

}
