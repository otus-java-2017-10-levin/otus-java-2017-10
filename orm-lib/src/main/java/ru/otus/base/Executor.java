package ru.otus.base;

import java.sql.*;
import java.util.List;

public class Executor {

    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public List<String> execQuery(String query) throws SQLException {
        try(Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
        return null;
    }

    public <T, E> List<E> execQuery(String query, ResultHandler<T> handler, List<E> type) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return null;
    }


    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }

    public void execUpdate(String update, ExecuteHandler prepare) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(update);
            prepare.accept(stmt);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return connection;
    }

    @FunctionalInterface
    public interface ExecuteHandler {
        void accept(PreparedStatement statement) throws SQLException;
    }

    @FunctionalInterface
    public interface ExecuteHandlerList {
        <E> List<E> accept(PreparedStatement statement, List<E> resultList);
    }
}
