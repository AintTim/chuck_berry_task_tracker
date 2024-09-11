package com.ainetdinov.tracker.model.request;

import com.ainetdinov.tracker.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskRequest implements RequestEntity {
    private String title;
    private String description;
    private Status status;
    private UserRequest assignee;
    private List<LabelRequest> labels;
}
