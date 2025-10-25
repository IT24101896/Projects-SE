-- 1. Create a dedicated database for your train reservation system.
CREATE DATABASE train_reservation_system;

-- 2. Select the database to use for the following commands.
USE train_reservation_system;

-- 3. Create the 'users' table to store user information.
-- Passwords are not stored directly; a hashed version is stored for security.
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE -- Used for deactivating accounts.
);
-- 4. Insert sample users for testing
INSERT INTO users (username, email, password, role, active, created_at, updated_at) VALUES
('john_doe', 'john@example.com', '$2a$10$exampleHashedPassword1', 'PASSENGER', TRUE, NOW(), NOW()),
('admin_user', 'admin@example.com', '$2a$10$exampleHashedPassword2', 'ADMIN', TRUE, NOW(), NOW()),
('staff_member', 'staff@example.com', '$2a$10$exampleHashedPassword3', 'STAFF', TRUE, NOW(), NOW());

-- Get total count of users for each role
SELECT 
    role,
    COUNT(*) as total_users,
    SUM(CASE WHEN active = TRUE THEN 1 ELSE 0 END) as active_users,
    SUM(CASE WHEN active = FALSE THEN 1 ELSE 0 END) as inactive_users
FROM users 
GROUP BY role;


-- Get all PASSENGERS
SELECT * FROM users WHERE role = 'PASSENGER';

-- Get all STAFF members  
SELECT * FROM users WHERE role = 'STAFF';

-- Get all ADMINS
SELECT * FROM users WHERE role = 'ADMIN';


-- Get detailed role statistics
SELECT 
    'PASSENGER' as role_type,
    (SELECT COUNT(*) FROM users WHERE role = 'PASSENGER') as total,
    (SELECT COUNT(*) FROM users WHERE role = 'PASSENGER' AND active = TRUE) as active,
    (SELECT COUNT(*) FROM users WHERE role = 'PASSENGER' AND active = FALSE) as inactive
    
UNION ALL

SELECT 
    'STAFF' as role_type,
    (SELECT COUNT(*) FROM users WHERE role = 'STAFF') as total,
    (SELECT COUNT(*) FROM users WHERE role = 'STAFF' AND active = TRUE) as active, 
    (SELECT COUNT(*) FROM users WHERE role = 'STAFF' AND active = FALSE) as inactive
    
UNION ALL

SELECT 
    'ADMIN' as role_type,
    (SELECT COUNT(*) FROM users WHERE role = 'ADMIN') as total,
    (SELECT COUNT(*) FROM users WHERE role = 'ADMIN' AND active = TRUE) as active,
    (SELECT COUNT(*) FROM users WHERE role = 'ADMIN' AND active = FALSE) as inactive;
    
    
-- to get details of table 
use train_reservation_system;
select * from users;

