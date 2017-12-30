package ru.otus.jdbc;

/**
 *
 */
public interface DbManager {

    DBConnection getConnection() throws IllegalArgumentException;
    void close();
    boolean isOpen();
}
