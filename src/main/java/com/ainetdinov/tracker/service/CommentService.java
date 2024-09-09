package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.generic.UpdateCommand;
import com.ainetdinov.tracker.model.dto.CommentDto;
import com.ainetdinov.tracker.model.entity.Comment;
import com.ainetdinov.tracker.model.mapper.CommentMapper;
import com.ainetdinov.tracker.repository.CommentRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentService extends EntityService<Comment> {
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        super(commentRepository);
        this.commentMapper = commentMapper;
    }

    public List<CommentDto> getAllComments() {
        var comments = getEntities();
        return toDtoList(comments);
    }

    public CommentDto getComment(Long id) {
        return commentMapper.toDto(getEntityById(id));
    }

    @Override
    public Serializable updateEntity(Comment comment) {
        return commentMapper.toDto(executeCommand(new UpdateCommand<>(repository, comment)));
    }

    private List<CommentDto> toDtoList(List<Comment> comments) {
        List<CommentDto> dtoList = new ArrayList<>();
        for (var comment : comments) {
            dtoList.add(commentMapper.toDto(comment));
        }
        return dtoList;
    }
}
