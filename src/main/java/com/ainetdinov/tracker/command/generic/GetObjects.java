package com.ainetdinov.tracker.command.generic;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.EntityDto;
import com.ainetdinov.tracker.model.entity.Source;
import com.ainetdinov.tracker.model.mapper.EntityMapper;
import com.ainetdinov.tracker.model.request.RequestEntity;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@RequiredArgsConstructor
public class GetObjects<E extends Source, D extends EntityDto, R extends RequestEntity> implements Command<List<D>> {
    private final AbstractRepository<E, Long> repository;
    private final EntityMapper<E, D, R> mapper;

    @Override
    public List<D> execute() {
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var objects = repository.findAll(session)
                    .stream().map(mapper::toDto)
                    .toList();
            transaction.commit();
            return objects;
        }
    }
}
