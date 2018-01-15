package ru.otus.persistence;


import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;

interface EntityVisitor {
    void visit(@NotNull ForeignKeys keys, @NotNull DBConnection connection);
    long visit(@NotNull EntityStructure structure, @NotNull DBConnection connection) throws IllegalAccessException;
     <T> T visit(@NotNull Class<T> entityClass, long primaryKey, @NotNull DBConnection connection);
}
