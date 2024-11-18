package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.request.TaskRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, CommentMapper.class, LabelMapper.class})
public interface TaskMapper extends EntityMapper<Task, TaskDto, TaskRequest> {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Override
    Task toEntity(TaskRequest taskRequest);

    @AfterMapping
    default void linkComments(@MappingTarget Task task) {
        task.getComments().forEach(comment -> comment.setTask(task));
    }

    @Override
    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}