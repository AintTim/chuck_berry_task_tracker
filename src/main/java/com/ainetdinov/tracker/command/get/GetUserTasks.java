package com.ainetdinov.tracker.command.get;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@RequiredArgsConstructor
public class GetUserTasks implements Command<List<TaskDto>> {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final UserRequest request;
    private final Status status;

    @Override
    public List<TaskDto> execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var tasks = repository.findUserTasks(session, request.getId(), status);
            transaction.commit();
            return tasks
                    .stream()
                    .map(mapper::toDto)
                    .toList();
        }
    }
}
