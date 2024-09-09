package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RemoveTask implements Command<Task> {
    private final AbstractRepository<Task, Long> repository;
    private final Long id;

    public RemoveTask(AbstractRepository<Task, Long> repository, Long id) {
        this.repository = repository;
        this.id = id;
    }

    @Override
    public Task execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var task = session.get(Task.class, id);

            task.removeComments();
            task.removeLabels();
            repository.delete(session, task);

            transaction.commit();
            return task;
        }
    }
}
