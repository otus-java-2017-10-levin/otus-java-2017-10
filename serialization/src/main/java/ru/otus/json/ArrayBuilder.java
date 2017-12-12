package ru.otus.json;


@SuppressWarnings("UnusedReturnValue")
public interface ArrayBuilder {
    ArrayBuilder add(Object object);
    Object build();
}
