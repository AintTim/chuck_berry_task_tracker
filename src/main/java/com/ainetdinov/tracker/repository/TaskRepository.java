package com.ainetdinov.tracker.repository;

import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.entity.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

import static com.ainetdinov.tracker.constant.WebConstant.*;

public class TaskRepository extends AbstractRepository<Task, Long> {

    public TaskRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Task> findByTitle(Session session, String title) {
        var query = session.createQuery("from Task t where t.title = :title", Task.class);
        query.setParameter(TITLE, title);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    public List<Task> findUserTasks(Session session, Long userId, Status status) {
        var query = session.createQuery("select t from Task t where t.assignee.id = :user and status = :status", Task.class);
        query.setParameter(USER, userId);
        query.setParameter(STATUS, status);
        return query.list();
    }

    public List<Task> findByStatus(Session session, Status status) {
        var query = session.createQuery("select t from Task t where t.status = :status", Task.class);
        query.setParameter(STATUS, status);
        return query.list();
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
