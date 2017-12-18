package ru.otus.jdbc;

/**
 *
 */
public interface DbManager {

    DBConnection createConnection() throws IllegalArgumentException;
}
