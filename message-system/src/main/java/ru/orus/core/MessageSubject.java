package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import ru.orus.messages.Message;

@SuppressWarnings("unused")
public interface MessageSubject {
    void subscribe(@NotNull String topic, @NotNull MessageObserver o);
    void unsubscribe(@NotNull String topic,@NotNull MessageObserver o);
    void notifyObservers(@NotNull Message<?> msg);
    void close();
}
