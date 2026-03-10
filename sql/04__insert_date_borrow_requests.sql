
INSERT INTO borrow_requests
(book_id, borrower_id, owner_id, status, requested_date, accepted_date, returned_date)
VALUES
(17, 5, 4, 'Pending', NOW(), NULL, NULL),  
(17, 3, 4, 'Accepted', '2026-01-05 10:00:00', '2026-01-06 12:00:00', NULL), 
(17, 5, 4, 'Rejected', '2026-01-03 09:00:00', NULL, NULL),
(17, 3, 4, 'Returned', '2025-12-28 15:00:00', '2025-12-29 10:00:00', '2026-01-01 11:00:00'),
(20, 5, 4, 'Pending', NOW(), NULL, NULL),
(20, 3, 4, 'Accepted', '2026-01-04 14:00:00', '2026-01-05 16:00:00', NULL); 