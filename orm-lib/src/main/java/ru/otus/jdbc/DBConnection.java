package ru.otus.jdbc;

import java.sql.SQLException;

public interface DBConnection extends AutoCloseable {

    void execQuery(String query)  throws IllegalArgumentException;
    void execQuery(String query, ExecutionHandler handler);
    <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException;
}
