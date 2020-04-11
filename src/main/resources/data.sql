INSERT INTO users (id, username, password, enabled) VALUES
(NULL, 'user0', 'pass', true);

INSERT INTO users (id, username, password, enabled) VALUES
(NULL, 'user', 'pass', true),
(NULL, 'admin', 'pass', true);

INSERT INTO authorities (username, authority) VALUES
('user', 'ROLE_USER'),
('admin', 'ROLE_ADMIN');
