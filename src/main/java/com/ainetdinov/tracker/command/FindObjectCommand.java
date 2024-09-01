package com.ainetdinov.tracker.command;

import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.NoSuchElementException;

public class FindObjectCommand<T, ID extends Serializable> implements Command<T> {
    private final AbstractRepository<T, ID> repository;
    private final ID id;

    public FindObjectCommand(AbstractRepository<T, ID> repository, ID id) {
        this.repository = repository;
        this.id = id;
    }

    @Override
    public T execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var object = repository.findById(session, id);
            transaction.commit();
            return object.orElseThrow(() -> new NoSuchElementException("Object not found by id: " + id));
        }
    }
}
