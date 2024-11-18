package com.ainetdinov.tracker.model.request;

import com.ainetdinov.tracker.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskRequest implements RequestEntity {
    private Long id;
    private String title;
    private String description;
    @Builder.Default
    private Status status = Status.OPEN;
    private UserRequest assignee;
    @Builder.Default
    private List<LabelRequest> labels = new ArrayList<>();
    @Builder.Default
    private List<CommentRequest> comments = new ArrayList<>();

    public TaskRequest(Long id) {
        this.id = id;
    }
}
