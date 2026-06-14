# Useful PostgreSQL commands

This file contains useful commands for working with the PostgreSQL database in this project.

## Connecting to the Database

### From host (when PostgreSQL container is running)

```
psql -h localhost -U admin -d psql_user
```

### From inside the Docker container

```bash
docker exec -it postgres_db psql -U admin -d psql_user
```

### General syntax

```
psql -U your_username -d your_database_name
```

## psql Meta-Commands

2. List all databases

```
\l
```

3. Connect to a different database

```
\c database_name
```

4. List all tables in the current database

```
\dt
```

5. Describe a table (show columns and data types)

```
\d table_name
```

Example (for this project's table):
```
\d br_user
```

6. Show current connection info

```
\conninfo
```

7. Quit psql

```
\q
```

## SQL Commands

### CREATE TABLE

```sql
CREATE TABLE table_name (
    column1 datatype1,
    column2 datatype2,
    ...
);
```

Example (this project's `br_user` table — created automatically by JPA, shown here for reference):
```sql
CREATE TABLE br_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    encrypt_password INTEGER NOT NULL
);
```

### INSERT

```sql
INSERT INTO table_name (column1, column2, ...)
VALUES (value1, value2, ...);
```

Example:
```sql
INSERT INTO br_user (name, email, username, encrypt_password)
VALUES ('John Doe', 'john@example.com', 'johndoe', 123456);

INSERT INTO br_user (name, email, username, encrypt_password)
VALUES ('Jane Smith', 'jane@example.com', 'janesmith', 654321);
```

### SELECT

```sql
-- Select all records
SELECT * FROM br_user;

-- Select specific columns
SELECT id, name, username FROM br_user;

-- Select with a condition
SELECT * FROM br_user WHERE username = 'johndoe';

-- Count records
SELECT COUNT(*) FROM br_user;

-- Order results
SELECT * FROM br_user ORDER BY name ASC;
```

### UPDATE

```sql
UPDATE table_name
SET column1 = value1, column2 = value2, ...
WHERE condition;
```

Example:
```sql
UPDATE br_user
SET email = 'newemail@example.com'
WHERE username = 'johndoe';
```

### DELETE

```sql
-- Delete a specific record
DELETE FROM br_user
WHERE username = 'johndoe';

-- Delete all records (keeps the table)
DELETE FROM br_user;
```

### TRUNCATE

```sql
-- Remove all rows and reset the ID sequence (faster than DELETE for clearing a table)
TRUNCATE TABLE br_user RESTART IDENTITY;
```

### DROP TABLE

```sql
-- Permanently remove the table and all its data
DROP TABLE IF EXISTS br_user;
```

## Useful Queries for This Project

```sql
-- Check if a user exists by username
SELECT EXISTS(SELECT 1 FROM br_user WHERE username = 'admin9');

-- Find users by partial name match
SELECT * FROM br_user WHERE name ILIKE '%lingkesh%';

-- Show table size
SELECT pg_size_pretty(pg_total_relation_size('br_user'));

-- Show all sequences (useful for checking auto-increment state)
SELECT * FROM pg_sequences WHERE schemaname = 'public';
```
