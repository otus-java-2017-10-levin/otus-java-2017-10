package ru.otus.persistence;


interface EntityVisitor {
    void visit(ForeignKeys keys);
    long visit(EntityStructure structure) throws IllegalAccessException;
}
