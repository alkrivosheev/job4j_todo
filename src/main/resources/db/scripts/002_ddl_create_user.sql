CREATE TABLE if not exists users (
                                     id SERIAL PRIMARY KEY,
                                     name TEXT,
                                     login TEXT,
                                     password TEXT
);