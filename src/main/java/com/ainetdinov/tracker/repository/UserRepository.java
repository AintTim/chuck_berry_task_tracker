package com.ainetdinov.tracker.repository;

import com.ainetdinov.tracker.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class UserRepository extends AbstractRepository<User, Long> {

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<User> findById(Session session, Long id) {
        return Optional.ofNullable(session.get(User.class, id));
    }

    @Override
    public List<User> findAll(Session session) {
        return session.createQuery("from User", User.class).list();
    }

    @Override
    public void save(Session session, User user) {
        session.persist(user);
    }

    @Override
    public void delete(Session session, User user) {
        session.remove(user);
    }

    @Override
    public User update(Session session, User user) {
        return session.merge(user);
    }
}
