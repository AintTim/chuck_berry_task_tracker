package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public class GetObjects<T> implements Command<List<T>> {
    private final AbstractRepository<T, ? extends Serializable> repository;

    public GetObjects(AbstractRepository<T, ? extends Serializable> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var objects = repository.findAll(session);
            transaction.commit();
            return objects;
        }
    }
}
