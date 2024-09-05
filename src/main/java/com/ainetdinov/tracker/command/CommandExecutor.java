package com.ainetdinov.tracker.command;

public interface CommandExecutor {

    default  <T> T executeCommand(Command<T> command) {
        return command.execute();
    }
}
