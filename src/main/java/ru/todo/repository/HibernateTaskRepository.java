package ru.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.todo.model.Task;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public Task save(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "FROM Task WHERE id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query("FROM Task ORDER BY id DESC", Task.class);
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return crudRepository.query(
                "FROM Task WHERE done = :fDone ORDER BY created DESC", Task.class,
                Map.of("fDone", done)
        );
    }

    @Override
    public boolean update(Task task) {
        try {
            Task mergedTask = crudRepository.tx(session -> session.merge(task));
            return mergedTask != null;
        } catch (Exception e) {
            log.error("Ошибка обновления задачи с id: {}", task.getId(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Task> task = findById(id);
        if (task.isEmpty()) {
            return false;
        }
        try {
            crudRepository.run(
                    "DELETE FROM Task WHERE id = :fId",
                    Map.of("fId", id)
            );
            return true;
        } catch (Exception e) {
            log.error("Ошибка удаления задачи с id: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean markAsDone(int id) {
        if (findById(id).isEmpty()) {
            return false;
        }
        crudRepository.run(
                "UPDATE Task SET done = true WHERE id = :fId",
                Map.of("fId", id));
        return true;
    }
}