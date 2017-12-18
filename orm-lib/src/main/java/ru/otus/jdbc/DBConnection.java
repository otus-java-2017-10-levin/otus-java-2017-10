package ru.otus.jdbc;

public interface DBConnection extends AutoCloseable {

    boolean execQuery(String create) throws IllegalArgumentException;
}
