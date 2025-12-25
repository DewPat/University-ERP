CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;

CREATE TABLE IF NOT EXISTS users_auth (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  role ENUM('ADMIN','INSTRUCTOR','STUDENT') NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  status ENUM('ACTIVE','LOCKED') DEFAULT 'ACTIVE',
  last_login DATETIME NULL
);
ALTER TABLE users_auth
ADD failed_attempts INT DEFAULT 0;
-- after creating both dbs
USE auth_db;
INSERT INTO users_auth
(username, role, password_hash, status, last_login, failed_attempts)
VALUES
('admin1', 'ADMIN', '$2a$10$gSsL2hhVmWOqdXxMfTeWcOdikL3pNZO1EkQGxEnU0hQUhs5TBWVXa', 'ACTIVE', NULL, 0),
('pankaj_', 'INSTRUCTOR', '$2a$10$y5kLSVGgVOIbc1.7LkU5IOx5ea8lcyDq3XQAg9fAmM06B1KAQx9n', 'ACTIVE', NULL, 0),
('anuj24024', 'STUDENT', '$2a$10$Wmu/..ELhkxyszlqzFj.zee6zTwTB3czNIx2Te7XMjpWEAPus2y30K', 'ACTIVE', NULL, 0),
('rohit24567', 'STUDENT', '$2a$10$uV7mDo53w0FM67EtNdCDpufbpEVP8HpAi/x2NiE2yEXCb0bnlNua', 'ACTIVE', NULL, 0),
('tillo_', 'INSTRUCTOR', '$2a$10$vme2gcreQpRLvsfo6IGnVu1RI4r8q0C7.R5B8wqMfAKYYnTxNyTp0', 'ACTIVE', NULL, 0),
('pandu24420', 'STUDENT', '$2a$10$..idbH2jijITt.GIEZDtJX.AfWyU1cHZFr7.G5581j2wZx1suQaX62', 'ACTIVE', NULL, 0),
('sanjit_', 'INSTRUCTOR', '$2a$10$acS9PA7ukjiPVNKa7PSBFuL1ECFGw8gxYpbcXGE6yYrS.pR7qSaIC', 'ACTIVE', NULL, 0),
('admin2', 'ADMIN', '$2a$10$QAfvkz0n64nE9rNBGXoUEu.cqCYkzwu/Zovz4Ae.IFQpU1s0CeQle', 'ACTIVE', NULL, 0),
('subhajit_', 'INSTRUCTOR', '$2a$10$5sobWJvjHVh171yATYeh3OA1ZZl9/ykrvyeDk2UdrXDCdmvYlAZWe', 'ACTIVE', NULL, 0),
('arun24100', 'STUDENT', '$2a$10$h/Fn/..qrW8H9pH3Se45.7m/WUAsrX55ySQUosdb5AJSDgZUKExm', 'ACTIVE', NULL, 0);

