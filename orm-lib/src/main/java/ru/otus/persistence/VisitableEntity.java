package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;

interface VisitableEntity {
    long save(@NotNull final EntityVisitor entityVisitor) throws IllegalAccessException;
    <T> T load(@NotNull final EntityVisitor entityVisitor, @NotNull final Class<T> entityClass, long id);
}
