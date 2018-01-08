package ru.otus.persistence;


public interface EntityVisitor {
    long visit(VisitableEntity visitable) throws IllegalAccessException;
}
