package com.ainetdinov.tracker.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ainetdinov.tracker.model.entity.Comment}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto implements Serializable, EntityDto {
    Long id;
    String comment;
}