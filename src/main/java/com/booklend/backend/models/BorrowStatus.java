package com.booklend.backend.models;

/**
 * Enum for borrow request status
 */

public enum BorrowStatus {
    Pending,
    Accepted,
    Rejected,
    Returned,
    Cancelled
}
