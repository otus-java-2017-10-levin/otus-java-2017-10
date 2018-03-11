package ru.orus.core;


import org.jetbrains.annotations.NotNull;

/**
 * Model of message consumer.
 * Has several callbacks methods for manage incoming messages.
 * Each Subscriber may have several active subscription.
 * @param <T> - message type
 */
interface Subscriber<T> {
    /**
     * Signals that no more message will be sent to this subscriber.
     */
    void onComplete();

    /**
     * Signals that error has been occurred.
     * @param throwable - error
     */
    void onError(@NotNull Throwable throwable);

    /**
     * Incoming message callback.
     * @param subscription - subscription that sends message
     * @param item - message
     */
    void onNext(@NotNull Subscription subscription, @NotNull T item);

    /**
     * Signals that subscription has been successfully applied
     * @param topic - topic of the subscription
     * @param subscription - subscription
     */
    void onSubscribe(@NotNull String topic, @NotNull Subscription subscription);
}
