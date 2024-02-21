INSERT INTO authorities (id, name) VALUES (1, 'ADMIN');
INSERT INTO authorities (id, name) VALUES (2, 'DOCTOR');
INSERT INTO authorities (id, name) VALUES (3, 'PATIENT');

WITH ins_admin AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, deleted)
    VALUES ('admin@system.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 1, false)
    RETURNING id
), ins_admin_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, deleted)
	SELECT ins_admin.id, 'System', 'Admin', 1, false
	FROM ins_admin
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 1, ins_admin.id FROM ins_admin;

-- Data modifying statements in WITH
WITH ins_doctor1 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, deleted)
    VALUES ('joefazer@gmail.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 2, false)
    RETURNING id
), ins_doctor1_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, deleted)
	SELECT ins_doctor1.id, 'Joe', 'Fazer', 1, false
	FROM ins_doctor1
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 2, ins_doctor1.id FROM ins_doctor1;

WITH ins_doctor2 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, deleted)
    VALUES ('janedoe@gmail.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 2, false)
    RETURNING id
), ins_doctor2_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, deleted)
	SELECT ins_doctor2.id, 'Jane', 'Doe', 2, false
	FROM ins_doctor2
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 2, ins_doctor2.id FROM ins_doctor2;

WITH ins_patient1 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, deleted)
    VALUES ('patrick@gmail.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 3, false)
    RETURNING id
), ins_patient1_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, deleted)
	SELECT ins_patient1.id, 'Patrick', 'Duenas', 1, false
	FROM ins_patient1
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 3, ins_patient1.id FROM ins_patient1;

WITH ins_patient2 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, deleted)
    VALUES ('monica@gmail.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 3, false)
    RETURNING id
), ins_patient2_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, deleted)
	SELECT ins_patient2.id, 'Monica', 'Lorenz', 2, false
	FROM ins_patient2
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 3, ins_patient2.id FROM ins_patient2;