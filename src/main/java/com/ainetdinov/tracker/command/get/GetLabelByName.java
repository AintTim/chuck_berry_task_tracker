package com.ainetdinov.tracker.command.get;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class GetLabelByName implements Command<Label> {
    private final LabelRepository repository;
    private final String name;

    @Override
    public Label execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var object = repository.findByName(session, name);
            transaction.commit();
            return object.orElse(null);
        }
    }
}
