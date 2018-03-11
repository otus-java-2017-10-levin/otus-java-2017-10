package ru.orus.core;

import org.jetbrains.annotations.NotNull;
import ru.orus.messages.Message;

class MessageSubscription extends AbstractMessageSubscription {
    MessageSubscription(@NotNull Subscriber<? super Message<?>> subscriber) {
        super(subscriber);
    }
}
