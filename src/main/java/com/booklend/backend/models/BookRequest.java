package com.booklend.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entity representing a book request posted by a user.
 * Maps directly to the `book_requests` table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "book_requests")
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookRequestId;

    @Column(nullable = false)
    private String title;

    private String author;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Solve low loading
    private Location location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private LocalDateTime createdAt;

    // Frontend-friendly display status
    public String getDisplayStatus() {
        switch (this.status) {
            case Pending: return "Pending";
            case This_book_is_available_now: return "This book is available now";
            case Cancelled: return "Cancelled";
            default: return "Unknown";
        }
    }
}
