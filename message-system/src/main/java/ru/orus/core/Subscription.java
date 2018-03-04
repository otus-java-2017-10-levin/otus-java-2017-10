package ru.orus.core;

public interface Subscription {
    void cancel();
    void request(long n);
}
