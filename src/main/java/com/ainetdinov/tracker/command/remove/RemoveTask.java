package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class RemoveTask implements Command<Task> {
    private final AbstractRepository<Task, Long> repository;
    private final TaskRequest object;

    @Override
    public Task execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var task = session.get(Task.class, object.getId());

            task.removeComments();
            task.removeLabels();
            repository.delete(session, task);

            transaction.commit();
            return task;
        }
    }
}
