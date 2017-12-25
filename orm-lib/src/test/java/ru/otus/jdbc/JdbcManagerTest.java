package ru.otus.jdbc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JdbcManagerTest {

    @Test
    void createConnectionTest() {
        DbManager manager = DbManagerFactory.createDataBaseManager(JdbcTestParams.properties);

        try (DBConnection connection = manager.createConnection()) {
            assertEquals(true, connection != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createConnectionWithWrongParamsTest() {
        Map<String, String> props = new HashMap<>();
        props.put(JdbcTestParams.DB_DRIVER_KEY, "org.postgresql.Driver");
        assertThrows(IllegalArgumentException.class, () -> DbManagerFactory.createDataBaseManager(props));
    }
}