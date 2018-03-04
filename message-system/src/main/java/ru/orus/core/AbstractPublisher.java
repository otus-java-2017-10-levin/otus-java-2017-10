package ru.orus.core;
/*
 *  @author Flow
 */

import ru.orus.common.Validation;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
class AbstractPublisher<T> implements Publisher<T>, Producer<T> {
    protected final Map<Subscriber<? super T>, MessageSubscription<? super T>> subscribers = new HashMap<>();

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        Validation.validateNonNull("", subscriber);

        if (subscribers.containsKey(subscriber))
            throw new IllegalStateException(subscriber + " already subscribed");

        final MessageSubscription<T> subscription = new MessageSubscription<>(subscriber);
        subscribers.put(subscriber, subscription);
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void submit(T item) {
        subscribers.forEach((subscriber, messageSubscription) -> messageSubscription.submit(item));
    }
}
