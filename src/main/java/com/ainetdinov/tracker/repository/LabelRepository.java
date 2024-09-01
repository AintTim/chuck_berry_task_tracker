package com.ainetdinov.tracker.repository;

import com.ainetdinov.tracker.model.entity.Label;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class LabelRepository extends AbstractRepository<Label, Long> {

    public LabelRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Label> findById(Session session, Long id) {
        return Optional.ofNullable(session.get(Label.class, id));
    }

    @Override
    public List<Label> findAll(Session session) {
        return session.createQuery("from Label", Label.class).list();
    }

    @Override
    public void save(Session session, Label label) {
        session.persist(label);
    }

    @Override
    public void delete(Session session, Label label) {
        session.remove(label);
    }

    @Override
    public Label update(Session session, Label label) {
        return session.merge(label);
    }
}
