package com.ainetdinov.tracker.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LabelRequest implements RequestEntity {
    private Long id;
    private String label;
    @Builder.Default
    private String color = "#808080";
    private List<TaskRequest> tasks;

    public LabelRequest(String label) {
        this.label = label;
    }

    public LabelRequest(Long id) {
        this.id = id;
    }
}
