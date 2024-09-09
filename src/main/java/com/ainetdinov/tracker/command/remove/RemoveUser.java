package com.ainetdinov.tracker.command.remove;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RemoveUser implements Command<User> {
    private final AbstractRepository<User, Long> repository;
    private final Long id;

    public RemoveUser(AbstractRepository<User, Long> repository, Long id) {
        this.repository = repository;
        this.id = id;
    }

    @Override
    public User execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var user = session.get(User.class, id);
            user.removeTasks();
            repository.delete(session, user);
            transaction.commit();

            return user;
        }
    }
}
