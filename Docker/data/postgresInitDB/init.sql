CREATE TABLE br_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    email VARCHAR,
    username VARCHAR NOT NULL,
    encrypt_password INT NOT NULL
);