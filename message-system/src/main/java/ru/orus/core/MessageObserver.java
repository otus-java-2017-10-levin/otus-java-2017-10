package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import ru.orus.messages.Message;

public interface MessageObserver {
    void update(@NotNull Message<?> msg);
    void close();
}
