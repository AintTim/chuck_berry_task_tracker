package com.ainetdinov.tracker.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {
    OPEN("open"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    private final String text;

    Status(String text) {
        this.text = text;
    }

    public static Status getStatus(String text) {
        return Arrays.stream(Status.values())
                .filter(s-> s.text.equals(text))
                .findFirst()
                .orElse(null);
    }
}
