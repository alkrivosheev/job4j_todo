package ru.todo.repository;


import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Repository;
import ru.todo.model.User;
import ru.todo.service.UserService;

import java.util.Optional;

@Slf4j
@Repository
public class HibernateUserRepository implements UserRepository, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Optional<User> save(User user) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return Optional.of(user);
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.error("Ошибка при сохранении пользователя с логином: {}", user.getLogin(), e);
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try (Session session = sf.openSession()) {
            User user = session.createQuery("FROM User WHERE login LIKE :fLogin AND password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
