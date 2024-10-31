-- Disable referential integrity temporarily
SET REFERENTIAL_INTEGRITY FALSE;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS wish_list_items;
DROP TABLE IF EXISTS wish_users;

-- Enable referential integrity again
SET REFERENTIAL_INTEGRITY TRUE;

-- Create `wish_users` table
CREATE TABLE wish_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Create `wish_list_items` table with a foreign key that references `wish_users`
CREATE TABLE wish_list_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    item_description VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES wish_users(id) ON DELETE CASCADE
);