package com.booklend.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing the status of a book request.
 */
public enum RequestStatus {
    Pending, // matches 'Pending' in the database
    This_book_is_available_now, // matches 'This book is available now' in the database
    Cancelled;

    /**
     * Converts the string representation of the status from the frontend to the corresponding Enum value.
     */
    @JsonCreator
    public static RequestStatus fromString(String status) {
        if(status.equalsIgnoreCase("Pending")) return Pending;
        if(status.equalsIgnoreCase("Cancelled")) return Cancelled;
        if(status.equalsIgnoreCase("This book is available now") || status.equalsIgnoreCase("This_book_is_available_now"))
            return This_book_is_available_now;
        throw new RuntimeException("Invalid status value: " + status);
    }
}