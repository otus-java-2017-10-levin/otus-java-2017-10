package ru.otus.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

class JdbcManager implements DbManager {

    private final Map<String, String> connectionData;
    private final List<DBConnection> connections = new ArrayList<>();
    private boolean isOpen;

    private final String DB_DRIVER = "javax.persistence.jdbc.driver";
    private final String DB_URL = "javax.persistence.jdbc.url";
    private final String DB_USER = "javax.persistence.jdbc.user";
    private final String DB_PASSWORD = "javax.persistence.jdbc.password";

    JdbcManager(Map<String, String> connectionData) {
        this.connectionData = connectionData;
        if (!validateConnectionData(connectionData))
            throw new IllegalArgumentException("Connection failed to db! Wrong params.");

        isOpen = true;
    }

    @Override
    public DBConnection getConnection(String name) {
        DBConnection con = getConnection(connectionData, name);
        connections.add(con);
        return con;
    }

    @Override
    public void close() {
        if (!isOpen)
            throw new IllegalStateException();


        for (DBConnection connection: connections) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void commitAll() throws SQLException {
        if (!isOpen)
            throw new IllegalStateException();

        for (DBConnection connection: connections) {
                connection.commit();
        }
    }

    @Override
    public void rollbackAll() throws SQLException {
        if (!isOpen)
            throw new IllegalStateException();

        for (DBConnection connection: connections) {
            connection.rollback();
        }
    }

    private DBConnection getConnection(Map<String, String> data, String name) {
        try {

            Class.forName(data.get(DB_DRIVER));
            return new JdbcConnection(DriverManager.getConnection(data.get(DB_URL),
                    data.get(DB_USER),
                    data.get(DB_PASSWORD)), name);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean validateConnectionData(Map<String, String> data) {
        return data.containsKey(DB_DRIVER) &&
                data.containsKey(DB_URL) &&
                data.containsKey(DB_USER) &&
                data.containsKey(DB_PASSWORD);
    }
}
