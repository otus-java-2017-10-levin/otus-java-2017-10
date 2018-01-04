package ru.otus.jdbc;

import java.sql.*;

class JdbcConnection implements  DBConnection {

    private static final String SCOPE_IDENTITY = "SELECT SCOPE_IDENTITY()";
    private final Connection connection;
    JdbcConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
        System.out.println("close");
    }

    @Override
    public void execQuery(String query) throws IllegalArgumentException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void execQuery(String query, ExecutionHandler handler) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            handler.accept(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        try(Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }

    /**
     * It returns the last IDENTITY value produced on a connection
     * and by a statement in the same scope, regardless of the table that produced the value.
     *
     * Supports only long type id
     *
     * @return - last added id
     */
    @Override
    public long getLastInsertedId() {
        long id = -1;
        try {
            id = execQuery(SCOPE_IDENTITY, result -> {
                result.next();
                return result.getLong(1);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
