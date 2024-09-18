package com.ainetdinov.tracker.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentRequest implements RequestEntity {
    private Long id;

    private String comment;

    public CommentRequest(String comment) {
        this.comment = comment;
    }
}
