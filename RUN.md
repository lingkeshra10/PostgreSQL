# Useful PostgreSQL commands

This file contains the useful commands that can run in PostgreSQL Database.

1. Login to PostgreSQL

```
psql -U your_username -d your_database_name
```

Example:
```
psql -U adminlingkesh -d psql_user
```

2. List the databases

```
\l
```

3. Connect to different database

```
\c database_name
```

4. List the tables in side the database

```
\dt
```

5. Describe the tables. Show the columns and datatype of columns

```
\d table_name
```

6. To create a table in a database

```
CREATE TABLE table_name (
    column1 datatype1,
    column2 datatype2,
    ...
);
```

Example:
```
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    age INTEGER,
    salary DECIMAL(10,2)
);
```

