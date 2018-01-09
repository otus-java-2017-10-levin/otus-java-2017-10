package ru.otus.persistence;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ForeignKeys implements VisitableEntity {
    private long id;
    private Class<?> entityClass;
    private Map<String, Long> keys = new HashMap<>();

    public ForeignKeys(long id, Class<?> entityClass) {
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
    public long apply(EntityVisitor visitableEntity) throws IllegalAccessException {
        visitableEntity.visit(this);
        return 0;
    }
}