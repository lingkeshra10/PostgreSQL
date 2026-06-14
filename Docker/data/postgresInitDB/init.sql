CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    email VARCHAR,
    username VARCHAR NOT NULL,
    encrypted_password VARCHAR(100) NOT NULL
);