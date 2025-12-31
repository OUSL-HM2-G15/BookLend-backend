package com.booklend.backend.repositories;

import com.booklend.backend.models.BorrowRequest;
import com.booklend.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface to handle BorrowRequest database operations.
 */
@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long>{
    
    // Get all borrowed books for logged-in user
    List<BorrowRequest> findByBorrower_UserId(int borrowerId);
    
    //Fetch all borrow requests for a particular book owner.
    List<BorrowRequest> findByOwner(User owner);

    // Optional: fetch by status if needed later
    List<BorrowRequest> findByBorrower_UserIdAndStatus(int borrowerId, String status);

}

