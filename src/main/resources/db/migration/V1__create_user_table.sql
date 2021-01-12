CREATE TABLE IF NOT EXISTS user_accounts (
    id serial NOT NULL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    gender VARCHAR(50),
    job_title VARCHAR(100)
)