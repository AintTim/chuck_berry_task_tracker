package com.ainetdinov.tracker.command.update;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.mapper.TaskMapper;
import com.ainetdinov.tracker.model.request.CommentRequest;
import com.ainetdinov.tracker.model.request.TaskRequest;
import com.ainetdinov.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UpdateTaskComments implements Command<TaskDto> {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskRequest request;

    @Override
    public TaskDto execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var task = repository.findById(session, request.getId()).orElseThrow();
            updateTaskComments(task);

            var updatedTask = repository.update(session, task);
            transaction.commit();
            return mapper.toDto(updatedTask);
        }
    }

    private void updateTaskComments(Task task) {
        var copy = new ArrayList<>(task.getComments());
        for (var comment : copy) {
            if (!containsComment(request.getComments(), comment)) {
                task.removeComment(comment);
            }
        }
        for (var comment : request.getComments()) {
            if (!containsComment(task.getComments(), comment)) {
                task.addComment(new Comment(comment.getComment()));
            }
        }
    }

    private boolean containsComment(List<CommentRequest> requests, Comment comment) {
        return requests
                .stream()
                .anyMatch(r -> r.getComment().equalsIgnoreCase(comment.getComment()));
    }

    private boolean containsComment(List<Comment> comments, CommentRequest comment) {
        return comments
                .stream()
                .anyMatch(c -> c.getComment().equalsIgnoreCase(comment.getComment()));
    }
}
