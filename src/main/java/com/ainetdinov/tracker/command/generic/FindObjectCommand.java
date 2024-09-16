package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.EntityDto;
import com.ainetdinov.tracker.model.entity.Source;
import com.ainetdinov.tracker.model.mapper.EntityMapper;
import com.ainetdinov.tracker.model.request.RequestEntity;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FindObjectCommand<E extends Source, D extends EntityDto, R extends RequestEntity> implements Command<D> {
    private final AbstractRepository<E, Long> repository;
    private final EntityMapper<E, D, R> mapper;
    private final R requestedObject;

    public FindObjectCommand(AbstractRepository<E, Long> repository, EntityMapper<E, D, R> mapper, R requestedObject) {
        this.repository = repository;
        this.mapper = mapper;
        this.requestedObject = requestedObject;
    }

    @Override
    public D execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var object = repository.findById(session, requestedObject.getId());
            transaction.commit();
            return object
                    .map(mapper::toDto)
                    .orElse(null);
        }
    }
}
