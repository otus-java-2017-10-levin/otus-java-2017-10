package ru.otus.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public DBConnection getConnection() throws IllegalArgumentException {
        DBConnection con = getConnection(connectionData);
        connections.add(con);
        return con;
    }

    @Override
    public void close() {
        if (!isOpen)
            throw new IllegalStateException();

        for (DBConnection connection : connections) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isOpen = false;
    }

    private DBConnection getConnection(Map<String, String> data) {
        try {

            Class.forName(data.get(DB_DRIVER));
            return new JdbcConnection(DriverManager.getConnection(data.get(DB_URL),
                    data.get(DB_USER),
                    data.get(DB_PASSWORD)));
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
