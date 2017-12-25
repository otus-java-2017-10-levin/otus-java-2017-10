package ru.otus.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ExecutionHandler {
    void accept(PreparedStatement statement) throws SQLException;
}