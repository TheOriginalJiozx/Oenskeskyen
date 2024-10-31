-- Disable referential integrity temporarily
SET REFERENTIAL_INTEGRITY FALSE;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS wishlist_items;
DROP TABLE IF EXISTS wish_users;
DROP TABLE IF EXISTS wish_items;

-- Enable referential integrity again
SET REFERENTIAL_INTEGRITY TRUE;

-- Create `wish_users` table
CREATE TABLE wish_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Create `wishlist_items` table with a foreign key that references `wish_users`
CREATE TABLE wishlist_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    item_description VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES wish_users(id) ON DELETE CASCADE
);

CREATE TABLE wish_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Unique identifier for each item
    item_name VARCHAR(100) NOT NULL,       -- Name of the item
    description TEXT,                      -- Description of the item
    category VARCHAR(50),                  -- Category or type of the item (e.g., "Electronics", "Books")
    price DECIMAL(10, 2),                  -- Price of the item
    stock_quantity INT DEFAULT 0,          -- Available stock for each item
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Date and time the item was added
);