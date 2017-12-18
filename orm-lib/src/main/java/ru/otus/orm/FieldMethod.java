package ru.otus.orm;

@FunctionalInterface
public interface FieldMethod<T> {
    T apply();
}
