package ru.todo.repository;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Repository;
import ru.todo.model.User;
import java.util.Optional;

@Slf4j
@Repository
public class HibernateUserRepository implements UserRepository, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public User save(User user) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            try {
                User existingUser = session.createQuery("FROM User WHERE login = :fLogin", User.class)
                        .setParameter("fLogin", user.getLogin())
                        .uniqueResult();

                if (existingUser != null) {
                    throw new IllegalArgumentException("Пользователь с логином '" + user.getLogin() + "' уже существует");
                }

                session.save(user);
                session.getTransaction().commit();
                return user;
            } catch (Exception e) {
                session.getTransaction().rollback();
                log.error("Failed to save user. Error: {}", e.getMessage(), e);
                throw e;
            }
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
