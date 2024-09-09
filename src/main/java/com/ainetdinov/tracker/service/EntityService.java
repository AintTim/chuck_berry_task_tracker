package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.CommandExecutor;
import com.ainetdinov.tracker.command.generic.CreateCommand;
import com.ainetdinov.tracker.command.generic.FindObjectCommand;
import com.ainetdinov.tracker.command.generic.GetObjects;
import com.ainetdinov.tracker.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;

import javax.naming.OperationNotSupportedException;
import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public abstract class EntityService<Entity> implements CommandExecutor {
    protected final AbstractRepository<Entity, Long> repository;

    protected List<Entity> getEntities() {
        var command = new GetObjects<>(repository);
        return executeCommand(command);
    }

    public Entity getEntityById(Long id) {
        var command = new FindObjectCommand<>(repository, id);
        return executeCommand(command);
    }

    public void createEntity(Entity entity) {
        var command = new CreateCommand<>(repository, entity);
        executeCommand(command);
    }

    public abstract Serializable updateEntity(Entity entity);

    public void deleteEntity(Long id) {
        throw new IllegalStateException("No implementation provided");
    };
}
