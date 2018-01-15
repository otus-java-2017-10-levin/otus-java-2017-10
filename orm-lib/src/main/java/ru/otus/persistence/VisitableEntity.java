package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;

interface VisitableEntity {

    long save(@NotNull final EntityVisitor entityVisitor,
              @NotNull DBConnection connection) throws IllegalAccessException;

    <T> T load(@NotNull final EntityVisitor entityVisitor,
               @NotNull final Class<T> entityClass,
               long id,
               @NotNull DBConnection connection);
}
