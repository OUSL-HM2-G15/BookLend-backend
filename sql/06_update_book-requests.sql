-- Purpose: Link book_requests to users table

START TRANSACTION;

ALTER TABLE book_requests
ADD COLUMN user_id INT;

-- To fill the null field in table if data inserted already
UPDATE book_requests
SET user_id = 1
WHERE book_request_id = 1;

ALTER TABLE book_requests
MODIFY user_id INT NOT NULL;

-- Add foreign key constraint
ALTER TABLE book_requests
ADD CONSTRAINT fk_book_requests_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON UPDATE CASCADE
ON DELETE CASCADE;

COMMIT;

ALTER TABLE book_requests 
MODIFY COLUMN status ENUM('Pending', 'This_book_is_available_now', 'Cancelled') DEFAULT 'Pending';
DESCRIBE book_requests;