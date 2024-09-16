package com.ainetdinov.tracker.command.create;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.repository.TaskRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CreateTask implements Command<TaskDto> {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskRequest taskRequest;
    private final List<String> labelNameList;

    public CreateTask(TaskRepository repository, TaskMapper mapper, TaskRequest taskRequest, List<String> labelNameList) {
        this.repository = repository;
        this.taskRequest = taskRequest;
        this.mapper = mapper;
        this.labelNameList = labelNameList;
    }

    @Override
    public TaskDto execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var user = findUserByName(session, taskRequest.getAssignee().getUsername()).uniqueResult();
            var labels = findLabelsByName(session, labelNameList).list();
            var task = mapper.toEntity(taskRequest);

            task.setAssignee(user);
            labels.forEach(task::addLabel);
            repository.save(session, task);
            transaction.commit();
            return mapper.toDto(task);
        }
    }

    private Query<User> findUserByName(Session session, String name) {
        var query = session.createQuery("from User u where username = :name", User.class);
        return query.setParameter("name", name);
    }

    private Query<Label> findLabelsByName(Session session, List<String> names) {
        var query = session.createQuery("from Label where label in (:names)", Label.class);
        return query.setParameterList("names", names);
    }
}
