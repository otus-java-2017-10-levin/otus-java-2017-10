package ru.orus.core;

public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}
