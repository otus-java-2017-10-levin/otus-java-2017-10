package ru.otus.orm;

import ru.otus.connection.DBConnection;

public class OrmLib {
    private final DBConnection connection;

    public OrmLib() {
        connection = new DBConnection();
    }

    /**
     * Create table for specified {@code class}
     * If there is no annotations in {@code cl} - throws IllegalArgumentException
     * Using annotations from javax.persistance.
     * @param cl - class from which we create a sql table
     * @throws IllegalArgumentException - throws if there is no annotations in this class
     */
    public void createTable(Class<?> cl) throws IllegalArgumentException {

    }


    /**
     * Save object to database
     * If there is no table for Object`s class throw an exception
     * @param object - object to save
     * @throws IllegalStateException - throws if error occurred
     */
    public void save(Object object) throws IllegalStateException {

    }
}
