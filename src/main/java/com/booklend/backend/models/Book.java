package com.booklend.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "books")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // To Avoid Infinite JSON Loop
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "user_id") // ID of the user who owns the book
    private Long userId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "fee_per_week")
    private BigDecimal feePerWeek;

    @Column(name = "status")
    private String status;   // Available / Unavailable
    
    @ManyToOne
    @JoinColumn(name = "available_location_id")
    private Location availableLocation;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "isbn", length = 17)
    private String isbn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "published_year")
    private Integer publishedYear;

    public Book() {
        // Default values
        this.createdAt = LocalDateTime.now();
        this.status = "Available";
        this.feePerWeek = BigDecimal.ZERO;
    }
}
