package ru.otus.orm;

import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps infomation about Entity class and its fields
 */

public class OrmRepresentation {
    private final Class<?> entityClass;
    private String tableName;
    private final Set<OrmField> fields = new HashSet<>();

    public OrmRepresentation(@NonNull Class<?> entityClass) throws IllegalArgumentException {
        if (entityClass == null) {
            throw new IllegalArgumentException("entity class is null");
        }
        this.entityClass = entityClass;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @NonNull
    public Class<?> getEntityClass() {
        return entityClass;
    }

    @NonNull
    public Set<OrmField> getFields() {
        return new HashSet<>(fields);
    }

    public void add(@NonNull OrmField field) {
        if (fields.contains(field)) {
            throw new IllegalArgumentException("field already exist");
        }
        fields.add(field);
    }
}
