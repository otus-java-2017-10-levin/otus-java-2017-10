package ru.otus.persistence;

interface VisitableEntity {
    long apply(EntityVisitor entityVisitor) throws IllegalAccessException;
}
