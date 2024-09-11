package com.ainetdinov.tracker.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LabelRequest implements RequestEntity {
    private String label;
    private String color;
}
