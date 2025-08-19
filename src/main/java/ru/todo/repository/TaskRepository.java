package ru.todo.repository;

import ru.todo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(int id);

    List<Task> findAll();

    List<Task> findByDone(boolean done);

    boolean update(Task task);

    boolean deleteById(int id);

    boolean markAsDone(int id);
}
