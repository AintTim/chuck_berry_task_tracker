package com.ainetdinov.tracker.repository;

import lombok.Getter;
import org.hibernate.SessionFactory;

import java.io.Serializable;

@Getter
public abstract class AbstractRepository<T, ID extends Serializable> implements Repository<T, ID> {
    private final SessionFactory sessionFactory;

    public AbstractRepository(SessionFactory session) {
        this.sessionFactory = session;
    }
    /*
    Command pattern:
    Receiver class - Repository<T, ID>
    Interface Command: R execute()

    Command class:
       <T, ID, R> SaveEntity<T, ID> implements Command {
            private Repository<T, ID> repository;
            private Session session;

            public SaveEntity(Session session, Repository repository) {
                this.session = session;
                this.repository = repository;
            }

            @Override
            public R execute() {
                Transaction transaction = session.beginTransaction();
                repository.save
            }


        }


     */


}
