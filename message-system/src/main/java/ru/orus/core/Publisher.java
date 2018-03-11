package ru.orus.core;

import org.jetbrains.annotations.NotNull;

/**
 * Message publisher model.
 * Generates messages for subscriptions.
 * One publisher may have several message topics and several subscription for each topic.
 * Creates a subscription for subscriber.
 *
 * @param <T>
 */
public interface Publisher<T> {

    /**
     * Create subscription for a specified {@code topic}.
     *
     * @param topic - topic to be subscribed.
     * @param subscriber - message consumer
     */
    void subscribe(@NotNull String topic, @NotNull Subscriber<? super T> subscriber);
}
