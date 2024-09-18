package com.ainetdinov.tracker.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ainetdinov.tracker.model.entity.Label}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabelDto implements Serializable, EntityDto {
    Long id;
    String label;
    String color;
}