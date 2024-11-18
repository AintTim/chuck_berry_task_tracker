package com.ainetdinov.tracker.command.get;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class GetTaskByTitle implements Command<Task> {
    private final TaskRepository taskRepository;
    private final String title;

    @Override
    public Task execute() {
        try (Session session = taskRepository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var task = taskRepository.findByTitle(session, title);
            transaction.commit();
            return task.orElse(null);
        }
    }
}
