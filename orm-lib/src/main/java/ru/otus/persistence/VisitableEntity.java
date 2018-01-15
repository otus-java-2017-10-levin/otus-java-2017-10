package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;

interface VisitableEntity {

    long save(@NotNull final EntityVisitor entityVisitor,
              @NotNull DBConnection connection) throws IllegalAccessException;
}
