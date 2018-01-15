package ru.otus.jdbc;

import java.sql.SQLException;

/**
 *
 */
public interface DbManager {

//    DBConnection getConnection() throws IllegalArgumentException;

    DBConnection getConnection(String name);

    void close();
    void commitAll() throws SQLException;
    void rollbackAll() throws SQLException;
}
