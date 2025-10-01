package ru.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.todo.repository.UserRepository;
import java.util.Optional;
import ru.todo.model.User;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> create(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
