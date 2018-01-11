package ru.otus.persistence;


import org.jetbrains.annotations.NotNull;

interface EntityVisitor {
    void visit(ForeignKeys keys);

    long visit(EntityStructure structure) throws IllegalAccessException;

     <T> T load(@NotNull Class<T> entityClass, long primaryKey);
}
