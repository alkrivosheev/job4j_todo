CREATE TABLE if not exists task_categories (
                                 task_id INT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
                                 category_id INT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
                                 PRIMARY KEY (task_id, category_id)
);