SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS wishlist_items;
DROP TABLE IF EXISTS wish_users;
DROP TABLE IF EXISTS wish_items;
DROP TABLE IF EXISTS item_categories;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE wish_users (
  id int NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  enabled tinyint(1) NOT NULL DEFAULT '1',
  times_donated int DEFAULT '0',
  PRIMARY KEY (id),
  UNIQUE KEY username (username)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE wishlist_items (
  id int NOT NULL AUTO_INCREMENT,
  user_id int NOT NULL,
  item_name varchar(255) NOT NULL,
  item_description varchar(255) DEFAULT NULL,
  price decimal(10,2) DEFAULT NULL,
  is_reserved tinyint(1) DEFAULT '0',
  PRIMARY KEY (id),
  KEY user_id (user_id),
  CONSTRAINT wishlist_items_ibfk_1 FOREIGN KEY (user_id) REFERENCES wish_users (id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE wish_items (
  id bigint NOT NULL AUTO_INCREMENT,
  item_name varchar(100) NOT NULL,
  item_description text,
  category varchar(50) DEFAULT NULL,
  price decimal(10,2) DEFAULT NULL,
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE item_categories (
  id bigint NOT NULL AUTO_INCREMENT,
  category_name varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO wish_users (username, password) VALUES ('Test', '1234');