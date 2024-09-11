package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.CommentDto;
import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.request.CommentRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper extends EntityMapper<Comment, CommentDto, CommentRequest>{

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Override
    Comment toEntity(CommentRequest commentRequest);

    @Override
    CommentDto toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);
}