ALTER TABLE borrow_requests
MODIFY COLUMN status ENUM('Pending', 'Accepted', 'Rejected', 'Returned', 'Cancelled') DEFAULT 'Pending';