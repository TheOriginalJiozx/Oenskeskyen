SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS wishlist_items;
DROP TABLE IF EXISTS wish_users;
DROP TABLE IF EXISTS wish_items;
DROP TABLE IF EXISTS item_categories;
DROP TABLE IF EXISTS reserved_items;

SET REFERENTIAL_INTEGRITY TRUE;

CREATE TABLE wish_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE wishlist_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    item_description VARCHAR(255),
    price DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES wish_users(id) ON DELETE CASCADE
);

CREATE TABLE wish_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    item_description TEXT,
    category VARCHAR(50),
    price DECIMAL(10, 2),
    stock_quantity INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE reserved_items (
    id SERIAL PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    amount INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES wish_users(id)
);