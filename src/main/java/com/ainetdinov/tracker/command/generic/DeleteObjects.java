package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Source;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class DeleteObjects<Entity extends Source> implements Command<Integer> {
    private final AbstractRepository<Entity, Long> repository;

    @Override
    public Integer execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int result = repository.deleteAll(session);
            transaction.commit();
            return result;
        }
    }
}
