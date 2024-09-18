package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.LabelDto;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class RemoveLabel implements Command<LabelDto> {
    private final AbstractRepository<Label, Long> repository;
    private final LabelMapper mapper;
    private final LabelRequest object;

    @Override
    public LabelDto execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var label = session.get(Label.class, object.getId());

            label.getTasks()
                    .forEach(task -> task.getLabels().remove(label));
            repository.delete(session, label);

            transaction.commit();
            return mapper.toDto(label);
        }
    }
}
