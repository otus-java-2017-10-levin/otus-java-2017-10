package ru.orus.core;

public interface Producer<T> {
    void submit(T item);
}
