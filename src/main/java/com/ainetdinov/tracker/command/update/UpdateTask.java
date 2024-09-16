package com.ainetdinov.tracker.command.update;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class UpdateTask implements Command<TaskDto> {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskRequest request;

    @Override
    public TaskDto execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var user = findUserByName(session, request.getAssignee().getUsername()).uniqueResult();
            var labels = findLabelsByName(session, request.getLabels()).list();
            var task = repository.findById(session, request.getId()).orElseThrow();

            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setAssignee(user);
            task.setLabels(labels);

            var updatedTask = repository.update(session, task);
            transaction.commit();
            return mapper.toDto(updatedTask);
        }
    }

    private Query<User> findUserByName(Session session, String name) {
        var query = session.createQuery("from User u where username = :name", User.class);
        return query.setParameter("name", name);
    }

    private Query<Label> findLabelsByName(Session session, List<LabelRequest> labels) {
        var query = session.createQuery("from Label where label in (:names)", Label.class);
        var names = labels.stream().map(LabelRequest::getLabel).toList();
        return query.setParameterList("names", names);
    }
}
