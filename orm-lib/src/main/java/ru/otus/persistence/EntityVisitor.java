package ru.otus.persistence;


import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;

interface EntityVisitor {
    void save(@NotNull ForeignKeys keys, @NotNull DBConnection connection);
    long save(@NotNull EntityStructure structure, @NotNull DBConnection connection) throws IllegalAccessException;
     <T> T load(@NotNull Class<T> entityClass, long primaryKey, @NotNull DBConnection connection);
}
