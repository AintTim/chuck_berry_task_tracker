package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.TaskDto;
import com.ainetdinov.tracker.model.entity.Task;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, CommentMapper.class, LabelMapper.class})
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task toEntity(TaskDto taskDto);

    @AfterMapping
    default void linkComments(@MappingTarget Task task) {
        task.getComments().forEach(comment -> comment.setTask(task));
    }

    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}