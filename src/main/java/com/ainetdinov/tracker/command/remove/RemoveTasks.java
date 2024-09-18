package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class RemoveTasks implements Command<Integer> {
    private final TaskRepository repository;

    @Override
    public Integer execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var tasks = repository.findAll(session);
            for (var task : tasks) {
                task.removeComments();
                task.removeLabels();
            }

            int result = repository.deleteAll(session);
            transaction.commit();
            return result;
        }
    }
}
