CREATE TABLE if not exists categories (
                            id SERIAL PRIMARY KEY,
                            name TEXT UNIQUE NOT NULL
);