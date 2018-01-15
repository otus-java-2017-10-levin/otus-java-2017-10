package ru.otus.jdbc;

import java.sql.SQLException;

public interface DBConnection extends AutoCloseable {

    void execQuery(String query)  throws IllegalArgumentException;
    void execQuery(String query, ExecutionHandler handler);
    <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException;

    /**
     * Commits changes for connection
     * @throws SQLException - if something went wrong
     * @throws IllegalStateException - if connection is already closed
     */
    void commit() throws SQLException;

    /**
     * Rolls back changes for connection
     * @throws SQLException - if something went wrong
     * @throws IllegalStateException - if connection is already closed
     */
    void rollback() throws SQLException;
}
