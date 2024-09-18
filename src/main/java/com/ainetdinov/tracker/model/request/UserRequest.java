package com.ainetdinov.tracker.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRequest implements RequestEntity {
    private Long id;
    private String username;
    private String password;

    public UserRequest(String username) {
        this.username = username;
    }
}
