INSERT INTO users (username, password, enabled) VALUES
('user', 'pass', true),
('admin', 'pass', true);

INSERT INTO authorities (username, authority) VALUES
('user', 'ROLE_USER'),
('admin', 'ROLE_ADMIN');
