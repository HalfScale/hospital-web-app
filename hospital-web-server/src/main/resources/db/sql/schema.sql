-- reference tables
CREATE TABLE authorities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE doctor_code (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255),
    specialization VARCHAR(255),
    description TEXT,
    created TIMESTAMP,
    modified TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP
);

--
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_type INTEGER,
    registration_token VARCHAR(255),
    datetime_password_reset TIMESTAMP,
    reset_pass_token VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    is_confirmed BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    created TIMESTAMP WITH TIME ZONE,
    modified TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN,
    deleted_date TIMESTAMP WITH TIME ZONE,
    authorities_id BIGINT,
    CONSTRAINT fk_authorities FOREIGN KEY (authorities_id) REFERENCES authorities(id) ON DELETE CASCADE
);

CREATE TABLE user_authorities (
    users_id BIGINT NOT NULL,
    authorities_id BIGINT NOT NULL,
    PRIMARY KEY (users_id, authorities_id),
    CONSTRAINT fk_user_authorities_users FOREIGN KEY (users_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_authorities_authorities FOREIGN KEY (authorities_id) REFERENCES authorities(id) ON DELETE CASCADE
);

CREATE TABLE user_details (
    id BIGSERIAL PRIMARY KEY,
    users_id BIGINT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    mobile_no VARCHAR(20),
    birth_date DATE,
    gender INTEGER,
    address TEXT,
    profile_image TEXT,
    doctor_code_id VARCHAR(255),
    doctor_description TEXT,
    no_of_years_experience INTEGER,
    education TEXT,
    schedule TEXT,
    expertise TEXT,
    created TIMESTAMP WITH TIME ZONE,
    modified TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_user_details_users FOREIGN KEY (users_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE hospital_rooms (
    id BIGSERIAL PRIMARY KEY,
    room_code VARCHAR(255),
    room_name VARCHAR(255),
    room_image TEXT,
    description TEXT,
    created_by BIGINT,
    updated_by BIGINT,
    created TIMESTAMP WITH TIME ZONE,
    modified TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE room_reservations (
    id BIGSERIAL PRIMARY KEY,
    hospital_room_id BIGINT,
    room_code VARCHAR(255),
    reserved_by_user_id BIGINT,
    has_associated_appointment_id BOOLEAN NOT NULL,
    associated_appointment_id BIGINT,
    reservation_status VARCHAR(255),
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT,
    created TIMESTAMP WITH TIME ZONE,
    modified TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_room_reservations_hospital_room FOREIGN KEY (hospital_room_id) REFERENCES hospital_rooms(id) ON DELETE SET NULL
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT,
    doctor_id BIGINT,
    appointment_status INTEGER,
    created TIMESTAMP,
    modified TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP,
    CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES user_details(id) ON DELETE SET NULL,
    CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES user_details(id) ON DELETE SET NULL
);

CREATE TABLE appointment_details (
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address TEXT,
    gender INTEGER,
    first_time BOOLEAN NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    mobile_no VARCHAR(20),
    email VARCHAR(255),
    appointment_reason TEXT,
    cancel_reason TEXT,
    created TIMESTAMP,
    modified TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP,
    CONSTRAINT fk_appointment_details_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);

CREATE TABLE appointment_details_history (
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT,
    appointment_details_id BIGINT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address TEXT,
    gender INTEGER,
    first_time BOOLEAN NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    mobile_no VARCHAR(20),
    email VARCHAR(255),
    appointment_reason TEXT,
    cancel_reason TEXT,
    created TIMESTAMP,
    modified TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP,
    CONSTRAINT fk_adh_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    CONSTRAINT fk_adh_details FOREIGN KEY (appointment_details_id) REFERENCES appointment_details(id) ON DELETE CASCADE
);

CREATE TABLE appointment_history (
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT,
    patient_id BIGINT,
    doctor_id BIGINT,
    appointment_status INTEGER,
    created TIMESTAMP,
    modified TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP,
    CONSTRAINT fk_ah_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    CONSTRAINT fk_ah_patient FOREIGN KEY (patient_id) REFERENCES user_details(id) ON DELETE SET NULL,
    CONSTRAINT fk_ah_doctor FOREIGN KEY (doctor_id) REFERENCES user_details(id) ON DELETE SET NULL
);

CREATE TABLE threads (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP
);

CREATE TABLE sender_users (
    id BIGSERIAL PRIMARY KEY,
    thread_id BIGINT,
    receiver_id BIGINT,
    sender_id BIGINT,
    created TIMESTAMP,
    modified TIMESTAMP,
    CONSTRAINT fk_sender_users_thread FOREIGN KEY (thread_id) REFERENCES threads(id) ON DELETE CASCADE
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    sender_users_id BIGINT,
    message TEXT,
    created TIMESTAMP,
    modified TIMESTAMP,
    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_users_id) REFERENCES sender_users(id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    receiver_id BIGINT,
    appointment_id BIGINT,
    status BIGINT NOT NULL,
    message TEXT,
    viewed TIMESTAMP,
    created TIMESTAMP,
    modified TIMESTAMP,
    deleted BOOLEAN NOT NULL,
    deleted_date TIMESTAMP,
    CONSTRAINT fk_notifications_receiver FOREIGN KEY (receiver_id) REFERENCES user_details(id) ON DELETE SET NULL,
    CONSTRAINT fk_notifications_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);
