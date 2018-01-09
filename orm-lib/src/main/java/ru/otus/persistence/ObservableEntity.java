package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;

public interface ObservableEntity {
    void attach(@NotNull Observer observer);
    void notifyObservers(long id);
}