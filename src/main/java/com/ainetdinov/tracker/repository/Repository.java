package com.ainetdinov.tracker.repository;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T, ID extends Serializable> {

    Optional<T> findById(Session session, ID id);

    List<T> findAll(Session session);

    void save(Session session, T t);

    void delete(Session session, T t);

    T update(Session session, T t);

    int deleteAll(Session session);
}
