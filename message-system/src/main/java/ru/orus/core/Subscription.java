package ru.orus.core;


/**
 * Subscription model. Keeps and sends messages for subscribers by theirs demand.
 * Publisher {@link Publisher} adds messages to subscription,
 * then {@link Subscriber} request for n messages. Then subscription sends next n incoming
 * messages to subscriber.
 *
 * Each subscription can be applied to a single subscriber.
 * If there are several subscribers for a single subscription - behaviour is undefined.
 */
public interface Subscription {

    /**
     * Cancel subscription for current subscriber
     */
    void cancel();

    /**
     * Request next n messages for subscriber.
     * @param n - num of messages.
     *          @throws IllegalArgumentException - if n <=0
     */
    void request(long n);
}
