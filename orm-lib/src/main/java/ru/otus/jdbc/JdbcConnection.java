package ru.otus.jdbc;

import java.sql.*;

class JdbcConnection implements  DBConnection {

    private final Connection connection;
    private final String name;
    private boolean autoCommit = false;
    JdbcConnection(Connection connection, String name) throws SQLException {
        this.connection = connection;
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        connection.setAutoCommit(autoCommit);
        this.name = name;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public void execQuery(String query) throws IllegalArgumentException {
        try (Statement stmt = connection.createStatement()) {
            System.out.println(query);
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execQuery(String query, ExecutionHandler handler) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            System.out.println(query);
            handler.accept(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        try(Statement stmt = connection.createStatement()) {
            System.out.println(query);
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }

    @Override
    public void commit() throws SQLException {
        if (!connection.isClosed()) {
            connection.commit();
            System.out.println("connection "+name+" committed");
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (!connection.isClosed())
            connection.rollback();
    }
}
