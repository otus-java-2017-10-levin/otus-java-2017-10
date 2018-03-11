package ru.orus.core.handlers;

import org.jetbrains.annotations.NotNull;
import ru.orus.core.AbstractMessageProcessor;
import ru.orus.messages.Message;

/**
 * Incoming message handler
 *
 * There are several types of messages {@link ru.orus.messages.Message.Type}
 * If message is system (e.q. subscribe for topic) message should be converted.
 *
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     *  Handle message.
     * @param message - incoming message
     * @return - new or just incoming {@code message}
     */
    @NotNull
    Message<?> handleMessage(@NotNull Message<?> message, AbstractMessageProcessor processor);
}