ALTER TABLE users
    ADD COLUMN IF NOT EXISTS username VARCHAR(80);

UPDATE users
SET username = LOWER(SPLIT_PART(email, '@', 1))
WHERE username IS NULL OR TRIM(username) = '';

ALTER TABLE users
    ALTER COLUMN username SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username_unique
    ON users (username);

CREATE INDEX IF NOT EXISTS idx_users_username
    ON users (username);