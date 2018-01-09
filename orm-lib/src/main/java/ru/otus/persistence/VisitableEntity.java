package ru.otus.persistence;

public interface VisitableEntity {
    long apply(EntityVisitor entityVisitor) throws IllegalAccessException;
}
