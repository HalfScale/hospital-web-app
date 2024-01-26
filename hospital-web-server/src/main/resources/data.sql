ALTER TABLE user_details ADD CONSTRAINT unique_user_id UNIQUE (users_id);
ALTER TABLE user_authorities ADD CONSTRAINT unique_user_authorities UNIQUE (authorities_id, users_id);

INSERT INTO authorities (name)
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE name = 'ADMIN');
INSERT INTO authorities (name)
SELECT 'DOCTOR' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE name = 'DOCTOR');
INSERT INTO authorities (name)
SELECT 'PATIENT' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE name = 'PATIENT');

INSERT INTO users (id, email, password, enabled, is_confirmed, user_type, deleted)
VALUES (1, 'admin@system.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 1, false)
ON CONFLICT (email) DO NOTHING;
INSERT INTO user_details(users_id, first_name, last_name, gender, deleted)
VALUES (1, 'System', 'Admin', 1, false)
ON CONFLICT (users_id) DO NOTHING;
INSERT INTO user_authorities(authorities_id, users_id)
VALUES (1, 1)
ON CONFLICT (authorities_id, users_id) DO NOTHING;