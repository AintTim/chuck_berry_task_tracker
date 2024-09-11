package com.ainetdinov.tracker.model.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRequest implements RequestEntity {
    private String username;
    private String password;
}
