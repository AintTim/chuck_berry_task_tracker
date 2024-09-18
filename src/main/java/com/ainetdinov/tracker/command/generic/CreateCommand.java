package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;

@RequiredArgsConstructor
public class CreateCommand<T> implements Command<T> {
    private final AbstractRepository<T, ? extends Serializable> repository;
    private final T object;

    @Override
    public T execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            repository.save(session, object);
            transaction.commit();
            return object;
        }
    }
}
