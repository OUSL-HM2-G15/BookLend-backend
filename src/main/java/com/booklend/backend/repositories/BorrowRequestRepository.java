package com.booklend.backend.repositories;
import com.booklend.backend.dto.LendedBookDto;
import com.booklend.backend.models.BorrowRequest;
import com.booklend.backend.models.BorrowStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface to handle BorrowRequest database operations.
 */

@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Integer> {

    // Get all borrow requests for a given owner, ordered by requestedDate descending
    List<BorrowRequest> findByOwner_UserIdOrderByRequestedDateDesc(int ownerId);

    // Find a single borrow request for an owner
    Optional<BorrowRequest> findByRequestIdAndOwner_UserId(int requestId, int ownerId);

    // Find all pending requests for an owner
    List<BorrowRequest> findByOwner_UserIdAndStatus(int ownerId, BorrowStatus status);

    // Custom query to fetch lended books details
    @Query("""
        SELECT new com.booklend.backend.dto.LendedBookDto(
            br.requestId,
            b.bookId,
            b.title,
            b.author,
            b.imageUrl,
            b.feePerWeek,
            borrower.fullName,
            loc.locationName,
            br.status,
            br.requestedDate,
            br.acceptedDate,
            br.returnedDate
        )
        FROM BorrowRequest br
        JOIN br.book b
        JOIN br.borrower borrower 
        LEFT JOIN borrower.location loc
        WHERE br.owner.userId = :ownerId
        ORDER BY br.requestedDate DESC
    """)
    List<LendedBookDto> findLendedBooksByOwner(@Param("ownerId") int ownerId);

    // Check if there is an active borrow request for a specific book
    boolean existsByBook_BookIdAndStatus(long bookId, BorrowStatus status);

    // Find all book IDs that have accepted borrow requests for a given owner
    @Query("""
        SELECT DISTINCT br.book.bookId
        FROM BorrowRequest br
        WHERE br.book.user.userId = :userId
        AND br.status = :status
    """)
    List<Long> findAcceptedBookIdsByOwner(@Param("userId") int userId, @Param("status") BorrowStatus status);

    /**
     * Get all borrow requests made by a borrower.
     * Used for "My Borrowed Books" page.
     */
    List<BorrowRequest> findByBorrower_UserId(int borrowerId);

    /**
     * Find a borrow request by ID for a borrower.
     * Can be used for canceling or closing a request.
     */
    Optional<BorrowRequest> findByRequestIdAndBorrower_UserId(int requestId, int borrowerId);

    List<BorrowRequest> findByBorrower_UserId(Long userId);
    
    public List<BorrowRequest> findByBorrower_UserIdAndStatusIn(int borrowerId, List<BorrowStatus> statuses);


}

