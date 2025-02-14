INSERT INTO authorities (id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'DOCTOR'),
    (3, 'PATIENT');

INSERT INTO doctor_code (code, specialization, description, created, modified, deleted, deleted_date)
VALUES
    ('0001IM', 'Internal Medicine', 'Specialists who apply scientific knowledge and clinical expertise to the diagnosis, treatment, and compassionate care of adults across the spectrum from health to complex illness. They are especially well trained in the diagnosis of puzzling medical problems, in the ongoing care of chronic illnesses, and in caring for patients with more than one disease. Internists also specialize in health promotion and disease prevention.', NOW(), NOW(), FALSE, NULL),
    ('0002PD', 'Pediatrician', 'Doctors who manage the health of your child, including physical, behavior, and mental health issues.', NOW(), NOW(), FALSE, NULL),
    ('0003SG', 'Surgeon', 'A physician who treats disease, injury, or deformity via operative or manual methods to physically change body tissues.', NOW(), NOW(), FALSE, NULL),
    ('0004OB', 'Obstetrician/Gynecologist', 'Doctors who specializes in women’s health. The female body experiences many different biological functions, including menstruation, childbirth, and menopause. OB-GYNs provide care for all of this and more.', NOW(), NOW(), FALSE, NULL),
    ('0005CD', 'Cardiologist', 'Doctors with special training and skill in finding, treating and preventing diseases of the heart and blood vessels.', NOW(), NOW(), FALSE, NULL),
    ('0006GSG', 'Gastroenterologist', 'Doctors who focuses on the health of the digestive system, or the gastrointestinal (GI) tract. Gastroenterologists can treat everything from irritable bowel syndrome (IBS) to hepatitis C.', NOW(), NOW(), FALSE, NULL),
    ('0007NG', 'Neurologist', 'Doctors who specializes in treating diseases of the nervous system.', NOW(), NOW(), FALSE, NULL);

WITH ins_admin AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, created, modified, deleted)
    VALUES ('admin@system.com', '$2a$10$XH5jMGgFVfgnrDinKnxF4.gy2vgM.Ipr9dQ.2TYwYfSqm2SZd2pKW', true, true, 1, NOW(),
    NOW(), false)
    RETURNING id
), ins_admin_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, created, modified, deleted)
	SELECT ins_admin.id, 'System', 'Admin', 1, NOW(), NOW(), false
	FROM ins_admin
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 1, ins_admin.id FROM ins_admin;

-- Data modifying statements in WITH
WITH ins_doctor1 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, created, modified, deleted)
    VALUES ('joefazer@gmail.com', '$2a$10$rfN6Z9FD.lUmDLgDfgKKBOKU89rb3KFR0Kytd9hkVLTKqDz4XQGs2', true, true, 2, NOW(),
    NOW(), false)
    RETURNING id
), ins_doctor1_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, created, modified, deleted)
	SELECT ins_doctor1.id, 'Joe', 'Fazer', 1, NOW(), NOW(), false
	FROM ins_doctor1
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 2, ins_doctor1.id FROM ins_doctor1;

WITH ins_doctor2 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, created, modified, deleted)
    VALUES ('janedoe@gmail.com', '$2a$10$rfN6Z9FD.lUmDLgDfgKKBOKU89rb3KFR0Kytd9hkVLTKqDz4XQGs2', true, true, 2, NOW(),
    NOW(), false)
    RETURNING id
), ins_doctor2_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, created, modified, deleted)
	SELECT ins_doctor2.id, 'Jane', 'Doe', 2, NOW(), NOW(),false
	FROM ins_doctor2
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 2, ins_doctor2.id FROM ins_doctor2;

WITH ins_patient1 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, created, modified, deleted)
    VALUES ('patrick@gmail.com', '$2a$10$rfN6Z9FD.lUmDLgDfgKKBOKU89rb3KFR0Kytd9hkVLTKqDz4XQGs2', true, true, 3, NOW(),
    NOW(), false)
    RETURNING id
), ins_patient1_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, created, modified, deleted)
	SELECT ins_patient1.id, 'Patrick', 'Duenas', 1, NOW(), NOW(), false
	FROM ins_patient1
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 3, ins_patient1.id FROM ins_patient1;

WITH ins_patient2 AS (
    INSERT INTO users (email, password, enabled, is_confirmed, user_type, created, modified, deleted)
    VALUES ('monica@gmail.com', '$2a$10$rfN6Z9FD.lUmDLgDfgKKBOKU89rb3KFR0Kytd9hkVLTKqDz4XQGs2', true, true, 3, NOW(),
    NOW(), false)
    RETURNING id
), ins_patient2_details AS (
	INSERT INTO user_details (users_id, first_name, last_name, gender, created, modified, deleted)
	SELECT ins_patient2.id, 'Monica', 'Lorenz', 2, NOW(), NOW(), false
	FROM ins_patient2
)
INSERT INTO user_authorities (authorities_id, users_id)
SELECT 3, ins_patient2.id FROM ins_patient2;