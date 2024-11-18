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
}
