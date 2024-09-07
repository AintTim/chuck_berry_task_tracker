package com.ainetdinov.tracker.model.dto;

import com.ainetdinov.tracker.constant.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.ainetdinov.tracker.model.entity.Task}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {
    Long id;
    String title;
    String description;
    Status status;
    UserDto assignee;
    List<CommentDto> comments;
    List<LabelDto> labels;
}