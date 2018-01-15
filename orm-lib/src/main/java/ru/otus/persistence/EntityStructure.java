package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;
import ru.otus.persistence.annotations.AnnotatedClass;

public class EntityStructure implements VisitableEntity {

    private final Object entity;
    private final AnnotatedClass entityClass;
    private long id;


    EntityStructure(@NotNull Object entity,
                    @NotNull AnnotatedClass entityClass) {
        this.entity = entity;
        this.entityClass = entityClass;
    }

    @Override
    public long save(@NotNull final EntityVisitor entityVisitor, @NotNull DBConnection connection) throws IllegalAccessException {
        return entityVisitor.visit(this, connection);
    }

    @Override
    public <T> T load(@NotNull EntityVisitor entityVisitor, @NotNull Class<T> entityClass, long id, @NotNull DBConnection connection) {
        return entityVisitor.visit(entityClass, id, connection);
    }

    public Object getEntity() {
        return entity;
    }

    public AnnotatedClass getEntityClass() {
        return entityClass;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EntityStructure{" +
                "entity=" + entity +
                ", entityClass=" + entityClass +
                '}';
    }
}