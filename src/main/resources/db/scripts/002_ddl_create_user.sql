CREATE TABLE if not exists users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255),
                                     login VARCHAR(255) UNIQUE,
                                     password VARCHAR(255),
                                     timezone VARCHAR(255)
);