package com.ainetdinov.tracker.command.update;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UpdateLabel implements Command<Label> {
    private final AbstractRepository<Label, Long> repository;
    private final Label object;

    public UpdateLabel(AbstractRepository<Label, Long> repository, Label object) {
        this.repository = repository;
        this.object = object;
    }

    @Override
    public Label execute() {
        Label updatedLabel;
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            updatedLabel = repository.update(session, object);
            transaction.commit();
            return updatedLabel;
        }
    }
}
