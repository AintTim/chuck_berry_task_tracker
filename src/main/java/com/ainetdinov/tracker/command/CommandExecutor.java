package com.ainetdinov.tracker.command;

public class CommandExecutor {

    public <T> T executeCommand(Command<T> command) {
        return command.execute();
    }
}
