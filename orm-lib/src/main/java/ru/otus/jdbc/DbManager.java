package ru.otus.jdbc;

/**
 *
 */
public interface DbManager {
    DBConnection getConnection();

    void close();
}
