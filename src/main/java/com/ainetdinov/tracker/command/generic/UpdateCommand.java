package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.EntityDto;
import com.ainetdinov.tracker.model.entity.Source;
import com.ainetdinov.tracker.model.mapper.EntityMapper;
import com.ainetdinov.tracker.model.request.RequestEntity;
import com.ainetdinov.tracker.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UpdateCommand<E extends Source, D extends EntityDto, R extends RequestEntity> implements Command<D> {
    private final AbstractRepository<E, Long> repository;
    private final EntityMapper<E, D, R> mapper;
    private final R object;

    public UpdateCommand(AbstractRepository<E, Long> repository, EntityMapper<E,D,R> mapper, R object) {
        this.repository = repository;
        this.mapper = mapper;
        this.object = object;
    }

    @Override
    public D execute() {
        D updated;
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            updated = mapper.toDto(repository.update(session, mapper.toEntity(object)));
            transaction.commit();
            return updated;
        }
    }
}
