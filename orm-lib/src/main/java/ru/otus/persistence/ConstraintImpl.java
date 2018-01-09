package ru.otus.persistence;

import ru.otus.persistence.annotations.AnnotatedClass;

public class ConstraintImpl implements Constraint {

    private AnnotatedClass table;
    private AnnotatedClass foreignTable;
    private String fieldName;

    public ConstraintImpl(AnnotatedClass table, AnnotatedClass foreignTable, String fieldName) {
        this.table = table;
        this.foreignTable = foreignTable;
        this.fieldName = fieldName;
    }

    /**
     * Gets table of constraint
     *
     * @return - class represent of a table
     */
    @Override
    public AnnotatedClass getTable() {
        return this.table;
    }

    /**
     * Get row name of a constraint
     *
     * @return - String name of a row
     */
    @Override
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Get class of a foreign table
     *
     * @return - class represent of a foreign table
     */
    @Override
    public AnnotatedClass getForeignTable() {
        return this.foreignTable;
    }
}


/*
            A

         B      C

      D     E      F



*/