ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);
ALTER TABLE user_details ADD CONSTRAINT unique_user_id UNIQUE (users_id);
ALTER TABLE user_authorities ADD CONSTRAINT unique_user_authorities UNIQUE (authorities_id, users_id);