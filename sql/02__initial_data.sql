-- -----------------------------------------------
-- Default Locations
-- -----------------------------------------------
INSERT INTO locations (location_name) VALUES
('Colombo'),
('Gampaha'),
('Kalutara'),
('Kandy'),
('Matale'),
('Nuwara Eliya'),
('Galle'),
('Matara'),
('Hambantota'),
('Jaffna'),
('Kilinochchi'),
('Mannar'),
('Vavuniya'),
('Mullaitivu'),
('Batticaloa'),
('Ampara'),
('Trincomalee'),
('Kurunegala'),
('Puttalam'),
('Anuradhapura'),
('Polonnaruwa'),
('Badulla'),
('Monaragala'),
('Ratnapura'),
('Kegalle');

-- -----------------------------------------------
-- Default Categories
-- -----------------------------------------------
INSERT INTO categories (category_name) VALUES
('Fiction'),
('Non-Fiction'),
('Education'),
('Biography'),
('Science'),
('History'),
('Self-Help'),
('Technology'),
('Children'),
('Comics');

-- -----------------------------------------------
-- Sample Users
-- -----------------------------------------------
INSERT INTO users (full_name, profile_pic, contact_no, whatsapp_no, email, location_id, created_at)
VALUES
('Admin User', NULL, '0710000000', '0710000000', 'admin@booklend.com', 1, NOW()),
('Sample Borrower', NULL, '0771111111', '0771111111', 'borrower@booklend.com', 2, NOW());

-- -----------------------------------------------
-- Sample Accounts
-- -----------------------------------------------
INSERT INTO account (username, role, password, user_id)
VALUES
('admin', 'admin', '$2a$10$samplehashedpassword', 1),
('borrower', 'user', '$2a$10$samplehashedpassword', 2);

-- -----------------------------------------------
-- Sample Books
-- -----------------------------------------------
INSERT INTO books (
    user_id, title, author, description, category_id, fee_per_week,
    status, available_location_id, image, isbn, created_at, published_year
)
VALUES
(1, 'The Great Gatsby', 'F. Scott Fitzgerald', 'Classic American novel', 1, 150.00,
 'Available', 1, NULL, '9780743273565', NOW(), 1925),

(1, 'Atomic Habits', 'James Clear', 'Self-improvement best seller', 6, 200.00,
 'Available', 1, NULL, '9780735211292', NOW(), 2018);

-- -----------------------------------------------
-- Sample Borrow Requests
-- -----------------------------------------------
INSERT INTO borrow_requests (
    book_id, borrower_id, owner_id, status, requested_date
)
VALUES
(1, 2, 1, 'Pending', NOW());

-- -----------------------------------------------
-- Sample Book Requests
-- -----------------------------------------------
INSERT INTO book_requests (title, author, location_id, status, created_at)
VALUES
('Harry Potter', 'J.K. Rowling', 2, 'Pending', NOW());

-- -----------------------------------------------
-- Sample Request Notifications
-- -----------------------------------------------
INSERT INTO book_request_notification (book_request_id, receiver_id, updated_at)
VALUES
(1, 1, NOW());