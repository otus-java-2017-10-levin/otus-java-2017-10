package ru.orus.core;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Command {
    void execute(@NotNull AbstractMessageProcessor processor);
}