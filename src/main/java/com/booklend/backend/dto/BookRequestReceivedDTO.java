package com.booklend.backend.dto;

import lombok.Getter;

@Getter
public class BookRequestReceivedDTO {

    private int requestId;
    private String title;
    private String author;
    private String locationName;
    private String requesterName;
    private String status;
    private String createdAt;

    public BookRequestReceivedDTO(
            int requestId,
            String title,
            String author,
            String locationName,
            String requesterName,
            String status,
            String createdAt
    ) {
        this.requestId = requestId;
        this.title = title;
        this.author = author;
        this.locationName = locationName;
        this.requesterName = requesterName;
        this.status = status;
        this.createdAt = createdAt;
    }
}
