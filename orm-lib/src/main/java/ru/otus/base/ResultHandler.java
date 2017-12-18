package ru.otus.base;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler<T> {
    T handle(ResultSet set) throws SQLException;
}
