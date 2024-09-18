package com.ainetdinov.tracker.model.dto;

import com.ainetdinov.tracker.constant.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.ainetdinov.tracker.model.entity.Task}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TaskDto implements Serializable, EntityDto {
    @EqualsAndHashCode.Include
    Long id;
    String title;
    String description;
    Status status;
    UserDto assignee;
    List<CommentDto> comments;
    List<LabelDto> labels;
}