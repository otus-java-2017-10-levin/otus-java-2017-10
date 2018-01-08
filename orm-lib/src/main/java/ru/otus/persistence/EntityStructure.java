package ru.otus.persistence;

import ru.otus.persistence.annotations.AnnotatedClass;

public class EntityStructure implements VisitableEntity {

    private final Object entity;
    private final AnnotatedClass entityClass;

    public EntityStructure(Object entity, AnnotatedClass entityClass) {
        this.entity = entity;
        this.entityClass = entityClass;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public AnnotatedClass getEntityClass() {
        return entityClass;
    }

    @Override
    public long visit(EntityVisitor entityVisitor) throws IllegalAccessException {
        return entityVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "EntityStructure{" +
                "entity=" + entity +
                ", entityClass=" + entityClass +
                '}';
    }
}
