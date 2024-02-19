Login to PostgreSQL

psql -U your_username -d your_database_name

psql -U adminlingkesh -d psql_user

Show tables under the database

SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;

Create User Table

CREATE TABLE br_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    email VARCHAR,
    username VARCHAR NOT NULL,
    encryptPassword INT NOT NULL
);

To see the column of the table

SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'br_user';

