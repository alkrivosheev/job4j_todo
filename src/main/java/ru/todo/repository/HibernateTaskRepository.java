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
        return crudRepository.query("FROM Task t LEFT JOIN FETCH t.priority ORDER BY t.id DESC", Task.class);
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return crudRepository.query(
                "FROM Task t JOIN FETCH t.priority WHERE done = :fDone ORDER BY created DESC", Task.class,
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
        try {
            int rowsAffected = crudRepository.executeUpdate(
                    "DELETE FROM Task WHERE id = :fId",
                    Map.of("fId", id));
            return rowsAffected > 0;
        } catch (Exception e) {
            log.error("Ошибка удаления задачи с id: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean markAsDone(int id) {
        try {
            int rowsAffected = crudRepository.executeUpdate(
                "UPDATE Task SET done = true WHERE id = :fId",
                Map.of("fId", id));
            return rowsAffected > 0;
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса задачи с id: {}", id, e);
            return false;
        }
    }
}