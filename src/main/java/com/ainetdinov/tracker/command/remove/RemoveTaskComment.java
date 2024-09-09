package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RemoveTaskComment implements Command<Comment> {
    private final AbstractRepository<Task, Long> repository;
    private final Long taskId;
    private final Long commentId;

    public RemoveTaskComment(AbstractRepository<Task, Long> repository, Long taskId, Long commentId) {
        this.repository = repository;
        this.taskId = taskId;
        this.commentId = commentId;
    }

    @Override
    public Comment execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            var task = session.get(Task.class, taskId);
            var comment = session.get(Comment.class, commentId);
            task.removeComment(comment);

            transaction.commit();
            return comment;
        }
    }
}
