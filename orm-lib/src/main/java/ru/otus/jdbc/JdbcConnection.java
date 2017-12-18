package ru.otus.jdbc;

import java.sql.Connection;

class JdbcConnection implements  DBConnection {

    private Connection connection;
    private JdbcConnection() {}

        @Override
    public void close() throws Exception {

    }

    @Override
    public boolean execQuery(String create) throws IllegalArgumentException {
        return false;
    }
}
