package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
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
    public long save(@NotNull final EntityVisitor entityVisitor) throws IllegalAccessException {
        return entityVisitor.visit(this);
    }

    @Override
    public <T> T load(@NotNull EntityVisitor entityVisitor, @NotNull Class<T> entityClass, long id) {
        return entityVisitor.visit(entityClass, id);
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