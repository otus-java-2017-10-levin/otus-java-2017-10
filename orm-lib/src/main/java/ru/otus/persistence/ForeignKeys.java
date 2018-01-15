package ru.otus.persistence;


import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;

import java.util.HashMap;
import java.util.Map;

public class ForeignKeys implements VisitableEntity {
    private final long id;
    private final Class<?> entityClass;
    private final Map<String, Long> keys = new HashMap<>();

    ForeignKeys(long id, Class<?> entityClass) {
        this.id = id;
        this.entityClass = entityClass;
    }

    public void addKey(@NotNull String name, long value) {
        keys.put(name, value);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public long getId() {
        return id;
    }

    public Map<String, Long> getKeys() {
        return keys;
    }

    @Override
    public long save(@NotNull final EntityVisitor visitableEntity, @NotNull DBConnection connection) {
        visitableEntity.visit(this, connection);
        return 0;
    }

    @Override
    public <T> T load(@NotNull EntityVisitor entityVisitor, @NotNull Class<T> entityClass, long id, @NotNull DBConnection connection) {
        throw new UnsupportedOperationException();
    }
}