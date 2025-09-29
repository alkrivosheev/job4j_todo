package ru.todo.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Service;
import ru.todo.model.Task;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HibernateTaskRepository implements TaskRepository, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Task save(Task task) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            try {
                session.persist(task);
                session.getTransaction().commit();
                return task;
            } catch (Exception e) {
                session.getTransaction().rollback();
                log.error("Failed to save task. Error: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        try (Session session = sf.openSession()) {
            Task task = session.createQuery("FROM Task WHERE id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResult();
            return Optional.ofNullable(task);
        }
    }

    @Override
    public List<Task> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task ORDER BY created DESC", Task.class)
                    .list();
        }
    }

    @Override
    public List<Task> findByDone(boolean done) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task WHERE done = :fDone ORDER BY created DESC", Task.class)
                    .setParameter("fDone", done)
                    .list();
        }
    }

    @Override
    public boolean update(Task task) {
        boolean isUpdated = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            try {
                session.update(task);
                session.getTransaction().commit();
                isUpdated = true;
            } catch (Exception e) {
                session.getTransaction().rollback();
                log.error("Failed to update task with id={}. Error: {}", task.getId(), e.getMessage(), e);
                throw e;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            try {
                int deletedCount = session.createQuery(
                                "DELETE Task WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate();
                session.getTransaction().commit();
                isDeleted = deletedCount > 0;
            } catch (Exception e) {
                session.getTransaction().rollback();
                log.error("Failed to delete task with id={}. Error: {}", id, e.getMessage(), e);
                throw e;
            }
        }
        return isDeleted;
    }

    @Override
    public boolean markAsDone(int id) {
        boolean isUpdated = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            try {
                int updatedCount = session.createQuery(
                                "UPDATE Task SET done = true WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate();
                session.getTransaction().commit();
                isUpdated = updatedCount > 0;
            } catch (Exception e) {
                session.getTransaction().rollback();
                log.error("Failed to mark task as done with id={}. Error: {}", id, e.getMessage(), e);
                throw e;
            }
        }
        return isUpdated;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}