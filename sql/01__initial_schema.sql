-- -----------------------------------------------------
-- Table: locations
-- -----------------------------------------------------
CREATE TABLE locations (
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    location_name VARCHAR(100) NOT NULL
);

-- -----------------------------------------------------
-- Table: categories
-- -----------------------------------------------------
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL
);

-- -----------------------------------------------------
-- Table: users
-- -----------------------------------------------------
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    profile_pic VARCHAR(255),
    contact_no VARCHAR(10),
    whatsapp_no VARCHAR(10),
    email VARCHAR(100),
    location_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_location
        FOREIGN KEY (location_id)
        REFERENCES locations(location_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Table: account
-- -----------------------------------------------------
CREATE TABLE account (
    username VARCHAR(50) PRIMARY KEY,
    role VARCHAR(10) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_id INT,
    CONSTRAINT fk_account_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: books
-- -----------------------------------------------------
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100),
    description TEXT,
    category_id INT,
    fee_per_week DECIMAL(10,2),
    status ENUM('Available', 'Unavailable') DEFAULT 'Available',
    available_location_id INT,
    image VARCHAR(255),
    isbn VARCHAR(17),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    published_year YEAR,
    CONSTRAINT fk_books_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_books_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_books_location
        FOREIGN KEY (available_location_id)
        REFERENCES locations(location_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Table: borrow_requests
-- -----------------------------------------------------
CREATE TABLE borrow_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    borrower_id INT NOT NULL,
    owner_id INT NOT NULL,
    status ENUM('Pending', 'Accepted', 'Rejected', 'Returned') DEFAULT 'Pending',
    requested_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    accepted_date DATETIME,
    returned_date DATETIME,
    CONSTRAINT fk_borrow_book
        FOREIGN KEY (book_id)
        REFERENCES books(book_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_borrow_borrower
        FOREIGN KEY (borrower_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_borrow_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: book_requests
-- -----------------------------------------------------
CREATE TABLE book_requests (
    book_request_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100),
    location_id INT,
    status ENUM('Pending', 'This book is available now') DEFAULT 'Pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookreq_location
        FOREIGN KEY (location_id)
        REFERENCES locations(location_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Table: book_request_notification
-- -----------------------------------------------------
CREATE TABLE book_request_notification (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    book_request_id INT NOT NULL,
    receiver_id INT NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_request
        FOREIGN KEY (book_request_id)
        REFERENCES book_requests(book_request_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_notification_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
