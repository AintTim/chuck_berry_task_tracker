package com.ainetdinov.tracker.command;

@FunctionalInterface
public interface Command<T> {
    T execute();
}
