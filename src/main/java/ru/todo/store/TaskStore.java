package ru.todo.store;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskStore {
    private final SessionFactory sf;

    @Autowired
    public TaskStore(SessionFactory sf) {
        this.sf = sf;
    }
}