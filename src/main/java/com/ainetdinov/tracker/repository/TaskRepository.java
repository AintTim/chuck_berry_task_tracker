package com.ainetdinov.tracker.repository;

import com.ainetdinov.tracker.model.entity.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class TaskRepository extends AbstractRepository<Task, Long> {

    public TaskRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Task> findById(Session session, Long id) {
        return Optional.ofNullable(session.get(Task.class, id));
    }

    @Override
    public List<Task> findAll(Session session) {
        return session.createQuery("from Task", Task.class).list();
    }

    @Override
    public void save(Session session, Task task) {
        session.persist(task);
    }

    @Override
    public void delete(Session session, Task task) {
        session.remove(task);
    }

    @Override
    public Task update(Session session, Task task) {
        return session.merge(task);
    }
}
