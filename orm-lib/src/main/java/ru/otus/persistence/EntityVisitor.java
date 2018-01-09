package ru.otus.persistence;


public interface EntityVisitor {
    void visit(ForeignKeys keys) throws IllegalAccessException;
    long visit(EntityStructure structure) throws IllegalAccessException;
}
