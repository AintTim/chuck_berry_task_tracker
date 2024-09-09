package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RemoveLabel implements Command<Label> {
    private final AbstractRepository<Label, Long> repository;
    private final Long id;

    public RemoveLabel(AbstractRepository<Label, Long> repository, Long id) {
        this.repository = repository;
        this.id = id;
    }

    @Override
    public Label execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var label = session.get(Label.class, id);

            label.getTasks()
                    .forEach(task -> task.getLabels().remove(label));
            repository.delete(session, label);

            transaction.commit();
            return label;
        }
    }
}
