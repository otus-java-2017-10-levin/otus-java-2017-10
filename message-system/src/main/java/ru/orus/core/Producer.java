package ru.orus.core;

import org.jetbrains.annotations.NotNull;

public interface Producer<T> {

    boolean submit(@NotNull T item);
}
