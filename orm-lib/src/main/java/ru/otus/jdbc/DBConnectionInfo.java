package ru.otus.jdbc;

public interface DBConnectionInfo {
    /**
     * It returns the last IDENTITY value produced on a connection
     * and by a statement in the same scope, regardless of the table that produced the value.
     * @return - last added id
     */
    long getLastInsertedId();
}
