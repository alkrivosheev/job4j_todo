package ru.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.todo.model.Task;
import ru.todo.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findActive() {
        return taskRepository.findByDone(false);
    }

    @Override
    public List<Task> findCompleted() {
        return taskRepository.findByDone(true);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public boolean completeTask(int id) {
        return taskRepository.markAsDone(id);
    }
}