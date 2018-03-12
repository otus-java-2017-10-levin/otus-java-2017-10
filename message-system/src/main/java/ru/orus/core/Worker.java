package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import ru.orus.messages.Message;

public interface Worker {
    void send(@NotNull Message<?> msg);

    void close();
}
