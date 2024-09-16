package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.CommandExecutor;
import com.ainetdinov.tracker.command.generic.CreateCommand;
import com.ainetdinov.tracker.command.generic.FindObjectCommand;
import com.ainetdinov.tracker.command.generic.GetObjects;
import com.ainetdinov.tracker.model.dto.EntityDto;
import com.ainetdinov.tracker.model.entity.Source;
import com.ainetdinov.tracker.model.mapper.EntityMapper;
import com.ainetdinov.tracker.model.request.RequestEntity;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@RequiredArgsConstructor
public abstract class EntityService<E extends Source, D extends EntityDto, R extends RequestEntity> implements CommandExecutor {
    protected final AbstractRepository<E, Long> repository;
    protected final EntityMapper<E, D, R> mapper;
    private final ResourceBundle messages;

    public List<D> getEntities() {
        var command = new GetObjects<>(repository, mapper);
        return executeCommand(command);
    }

    public D getEntityById(R requestEntity) {
        var command = new FindObjectCommand<>(repository, mapper, requestEntity);
        return executeCommand(command);
    }

    public void createEntity(R requestEntity) {
        var command = new CreateCommand<>(repository, mapper.toEntity(requestEntity));
        executeCommand(command);
    }

    public String getMessage(String key) {
        return messages.getString(key);
    }

    public <Entity> boolean validateEntity(Entity entity, Predicate<Entity> validation) {
        return validation.test(entity);
    }

    public D updateEntity(R requestEntity) {
        throw new IllegalStateException("No implementation provided");
    }

    public void deleteEntity(R requestEntity) {
        throw new IllegalStateException("No implementation provided");
    };
}
