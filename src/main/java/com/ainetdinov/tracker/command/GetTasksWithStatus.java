package com.ainetdinov.tracker.command;

import com.ainetdinov.tracker.constant.Status;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public class GetTasksWithStatus implements Command<List<Task>> {
    private final AbstractRepository<Task, ? extends Serializable> repository;
    private final Status status;

    public GetTasksWithStatus(AbstractRepository<Task, ? extends Serializable> repository, Status status) {
        this.repository = repository;
        this.status = status;
    }

    @Override
    public List<Task> execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var objects = repository.findAll(session);
            var filteredObjects = objects.stream()
                    .filter(t -> t.getStatus().equals(status))
                    .toList();
            transaction.commit();
            return filteredObjects;
        }
    }
}
