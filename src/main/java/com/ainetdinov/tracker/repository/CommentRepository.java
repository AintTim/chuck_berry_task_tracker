package com.ainetdinov.tracker.repository;

import com.ainetdinov.tracker.model.entity.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class CommentRepository extends AbstractRepository<Comment, Long> {

    public CommentRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Comment> findById(Session session, Long id) {
        return Optional.ofNullable(session.get(Comment.class, id));
    }

    @Override
    public List<Comment> findAll(Session session) {
        return session.createQuery("from Comment", Comment.class).list();
    }

    @Override
    public void save(Session session, Comment comment) {
        session.persist(comment);
    }

    @Override
    public void delete(Session session, Comment comment) {
        session.remove(comment);
    }

    @Override
    public Comment update(Session session, Comment comment) {
        return session.merge(comment);
    }
}
