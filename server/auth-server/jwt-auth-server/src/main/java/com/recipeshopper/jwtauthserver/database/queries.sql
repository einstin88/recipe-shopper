-- Add new user
INSERT INTO users(
        id,
        username,
        PASSWORD,
        first_name,
        last_name,
        email
    )
VALUES (?, ?, ?, ?, ?, ?);

-- Find user by username
SELECT *
FROM users
WHERE username = ?;

-- Find authorities by username
SELECT *
FROM user_authorities
WHERE username = ?;