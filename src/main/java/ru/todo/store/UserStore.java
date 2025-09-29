package ru.todo.store;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserStore {
    private final SessionFactory sf;

    @Autowired
    public UserStore(SessionFactory sf) {
        this.sf = sf;
    }
}
