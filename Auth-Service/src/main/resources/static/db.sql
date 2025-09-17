CREATE TABLE roles (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(100) NOT NULL UNIQUE,
email VARCHAR(150) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
enabled BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE user_roles (
user_id BIGINT NOT NULL,
role_id BIGINT NOT NULL,
PRIMARY KEY (user_id, role_id),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);


-- seed roles and an admin user (passwords should be bcrypt in real apps; here it's plain for sample - but we'll show how to store bcrypt hashes)
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');


-- sample bcrypt password for 'admin123' (we will show how to encode in Java) - placeholder here
INSERT INTO users (username, email, password, enabled) VALUES
('admin', 'admin@example.com', '$2a$10$REPLACEMEWITHBCRYPTHASH', true),
('user1', 'user1@example.com', '$2a$10$REPLACEMEWITHBCRYPTHASH', true);


-- link roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 2), -- admin -> ROLE_ADMIN
(2, 1); -- user1 -> ROLE_USER