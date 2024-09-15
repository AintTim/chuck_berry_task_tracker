package com.ainetdinov.tracker.command.get;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@RequiredArgsConstructor
public class GetTasksWithStatus implements Command<List<TaskDto>> {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final Status status;

    @Override
    public List<TaskDto> execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var tasks = repository.findByStatus(session, status);
            transaction.commit();
            return tasks
                    .stream()
                    .map(mapper::toDto)
                    .toList();
        }
    }
}
