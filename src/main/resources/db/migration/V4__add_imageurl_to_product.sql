ALTER TABLE products
    ADD image_url VARCHAR(2048);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users
    ALTER COLUMN username SET NOT NULL;