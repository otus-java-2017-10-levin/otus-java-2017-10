package ru.otus.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

class JdbcConnection implements  DBConnection {

    private final Connection connection;
    JdbcConnection(Connection connection) {
        this.connection = connection;
    }

        @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public boolean execQuery(String query) throws IllegalArgumentException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void execQuery(String query, ExecutionHandler handler) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            handler.accept(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
