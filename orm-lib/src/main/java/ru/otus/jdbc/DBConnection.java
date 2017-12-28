package ru.otus.jdbc;

import java.sql.SQLException;

public interface DBConnection extends DBConnectionInfo, AutoCloseable {

    boolean execQuery(String query)  throws IllegalArgumentException;
    void execQuery(String query, ExecutionHandler handler);
    <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException;
}
