INSERT INTO priorities (name, position) VALUES ('urgently', 1);
INSERT INTO priorities (name, position) VALUES ('normal', 2);

INSERT INTO users (login, password, name) VALUES ('test@mail.ru', 123, 'Alexander');

INSERT INTO tasks (description, created, done, user_id, priority_id)
VALUES (
           'Тестовое задание',CURRENT_TIMESTAMP,FALSE,1,1
       );