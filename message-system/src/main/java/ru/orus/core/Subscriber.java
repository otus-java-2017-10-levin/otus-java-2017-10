package ru.orus.core;

public interface Subscriber<T> {
    void onComplete();
    void onError(Throwable throwable);
    void onNext(Subscription subscription, T item);
    void onSubscribe(Subscription subscription);
}
