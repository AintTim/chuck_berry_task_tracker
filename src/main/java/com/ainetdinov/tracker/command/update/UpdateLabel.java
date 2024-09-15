package com.ainetdinov.tracker.command.update;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.LabelDto;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class UpdateLabel implements Command<LabelDto> {
    private final LabelRepository repository;
    private final LabelMapper mapper;
    private final LabelRequest request;


    @Override
    public LabelDto execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var label = repository.findByName(session, request.getLabel()).orElseThrow();
            label.setColor(request.getColor());
            var updated = repository.update(session, label);
            transaction.commit();
            return mapper.toDto(updated);
        }
    }
}
