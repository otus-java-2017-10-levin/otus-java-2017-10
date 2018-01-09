package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.persistence.annotations.AnnotatedClass;

public class EntityStructure implements VisitableEntity {

    private final Object entity;
    private final AnnotatedClass entityClass;


    EntityStructure(@NotNull Object entity,
                    @NotNull AnnotatedClass entityClass) {
        this.entity = entity;
        this.entityClass = entityClass;
    }

    @Override
    public long apply(EntityVisitor entityVisitor) throws IllegalAccessException {
        return entityVisitor.visit(this);
    }

    public Object getEntity() {
        return entity;
    }

    public AnnotatedClass getEntityClass() {
        return entityClass;
    }

    @Override
    public String toString() {
        return "EntityStructure{" +
                "entity=" + entity +
                ", entityClass=" + entityClass +
                '}';
    }
}