CREATE TABLE if not exists tasks (
                       id SERIAL PRIMARY KEY,
                       description TEXT,
                       created TIMESTAMP,
                       done BOOLEAN,
                       user_id INT NOT NULL REFERENCES users(id)
);