package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;

public class UpdateCommand<T> implements Command<T> {
    private final AbstractRepository<T, ? extends Serializable> repository;
    private final T object;

    public UpdateCommand(AbstractRepository<T, ? extends Serializable> repository, T object) {
        this.repository = repository;
        this.object = object;
    }

    @Override
    public T execute() {
        T updatedEntity;
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            updatedEntity = repository.update(session, object);
            transaction.commit();
            return updatedEntity;
        }
    }
}
