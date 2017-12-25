package ru.otus.jdbc;

public interface DBConnection extends AutoCloseable {

    boolean execQuery(String query)  throws IllegalArgumentException;
    void execQuery(String query, ExecutionHandler handler);
}
