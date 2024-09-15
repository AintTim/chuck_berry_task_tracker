package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class RemoveUser implements Command<User> {
    private final AbstractRepository<User, Long> repository;
    private final UserRequest object;

    @Override
    public User execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var user = session.get(User.class, object.getId());
            user.removeTasks();
            repository.delete(session, user);
            transaction.commit();
            return user;
        }
    }
}
