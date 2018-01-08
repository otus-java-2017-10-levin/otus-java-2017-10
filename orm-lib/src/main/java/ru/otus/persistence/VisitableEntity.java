package ru.otus.persistence;

import ru.otus.persistence.annotations.AnnotatedClass;

public interface VisitableEntity {

    long visit(EntityVisitor entityVisitor) throws IllegalAccessException;
    AnnotatedClass getEntityClass();
    public Object getEntity();
}
