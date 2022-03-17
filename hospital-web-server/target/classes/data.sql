INSERT INTO todo (name, description, completed) VALUES ('React', 'Learn react elements', false);
INSERT INTO todo (name, description, completed) VALUES ('React', 'Learn react components and props', false);
INSERT INTO todo (name, description, completed) VALUES ('React', 'Learn react state and lifecycle methods', false);

--$2a$10$mvSHuAGSaEmaSfZb1J8BaOoDvXNcXWTomwWWeVzgCqiuXFFuJJvxq

INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO user (username, password, role_id) VALUES ('marwin@gmail.com', '$2a$10$KKcVr1d6QIMv4SiiA6HP5uj8prhpadSvJItggd3mKDGV4sddwrfQC', 1);
INSERT INTO user (username, password, role_id) VALUES ('admin@gmail.com', '$2a$10$KKcVr1d6QIMv4SiiA6HP5uj8prhpadSvJItggd3mKDGV4sddwrfQC', 2);

