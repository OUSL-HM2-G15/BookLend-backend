-- Sample books with valid owner IDs and web images
INSERT INTO books
(book_id, user_id, title, author, description, category_id, fee_per_week, status, available_location_id, image, isbn, published_year)
VALUES
(101, 1, 'Java Basics', 'James Gosling', 'Learn Java programming from scratch', 1, 50.00, 'Available', 1, 
'https://images-na.ssl-images-amazon.com/images/I/41jEbK-jG+L._SX258_BO1,204,203,200_.jpg', '978-0135166307', 2020),

(102, 2, 'Spring Boot in Action', 'Craig Walls', 'Comprehensive guide to Spring Boot', 1, 70.00, 'Available', 1, 
'https://images-na.ssl-images-amazon.com/images/I/51k7HX8lyXL._SX377_BO1,204,203,200_.jpg', '978-1617292545', 2021),

(103, 3, 'Effective Java', 'Joshua Bloch', 'Best practices for Java programming', 1, 80.00, 'Available', 2, 
'https://images-na.ssl-images-amazon.com/images/I/41jWcXf8kPL._SX376_BO1,204,203,200_.jpg', '978-0134685991', 2018),

(104, 7, 'Clean Code', 'Robert C. Martin', 'A Handbook of Agile Software Craftsmanship', 1, 75.00, 'Available', 2,
'https://images-na.ssl-images-amazon.com/images/I/41xShlnTZTL._SX374_BO1,204,203,200_.jpg', '978-0132350884', 2008);



-- Sample borrow_requests for user 12 only (valid IDs)

INSERT INTO borrow_requests
(book_id, borrower_id, owner_id, status, requested_date, accepted_date, returned_date)
VALUES
-- Pending requests
(101, 12, 1, 'Pending', NOW(), NULL, NULL),
(102, 12, 2, 'Pending', NOW(), NULL, NULL),

-- Accepted request
(103, 12, 3, 'Accepted', '2026-01-05 10:00:00', '2026-01-06 12:00:00', NULL),

-- Rejected request
(104, 12, 7, 'Rejected', '2026-01-03 09:00:00', NULL, NULL),

-- Returned request
(101, 12, 1, 'Returned', '2025-12-28 15:00:00', '2025-12-29 10:00:00', '2026-01-01 11:00:00');
