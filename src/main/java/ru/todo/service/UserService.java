package ru.todo.service;

import ru.todo.model.User;
import java.util.Optional;

public interface  UserService {
    User create(User user);

    Optional<User> findByLoginAndPassword(String login, String password);
}
