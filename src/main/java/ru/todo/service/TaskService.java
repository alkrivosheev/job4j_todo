package ru.todo.service;

import ru.todo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task create(Task task);

    Optional<Task> findById(int id);

    List<Task> findAll();

    List<Task> findActive();

    List<Task> findCompleted();

    boolean update(Task task);

    boolean delete(int id);

    boolean completeTask(int id);
}