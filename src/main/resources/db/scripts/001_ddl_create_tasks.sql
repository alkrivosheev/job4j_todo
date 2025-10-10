CREATE TABLE if not exists tasks (
                       id SERIAL PRIMARY KEY,
                       description VARCHAR(255),
                       created TIMESTAMP WITH TIME ZONE,
                       done BOOLEAN,
                       user_id INT NOT NULL REFERENCES users(id),
                       priority_id INT REFERENCES priorities(id)
);