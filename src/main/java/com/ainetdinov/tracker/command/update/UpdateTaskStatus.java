package com.ainetdinov.tracker.command.update;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class UpdateTaskStatus implements Command<TaskDto> {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskRequest request;

    @Override
    public TaskDto execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var task = repository.findById(session, request.getId()).orElseThrow();
            task.setStatus(request.getStatus());
            var updated = repository.update(session, task);
            transaction.commit();
            return mapper.toDto(updated);
        }
    }
}
