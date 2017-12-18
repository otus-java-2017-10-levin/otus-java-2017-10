package ru.otus.jdbc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JdbcManagerTest {

    @Test
    void createConnectionTest() {
        DbManager manager = DbManagerFactory.createDataBaseManager(new HashMap<>());

        assert manager != null;
        try (DBConnection connection = manager.createConnection()) {
            assertEquals(true, connection != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createConnectionWithWrongParamsTest() {
        DbManager manager = DbManagerFactory.createDataBaseManager(new HashMap<>());

        Exception e = assertThrows(IllegalArgumentException.class, () -> manager.createConnection());
        assertEquals("Connection failed to db! Wrong params.", e.getMessage());
    }
}