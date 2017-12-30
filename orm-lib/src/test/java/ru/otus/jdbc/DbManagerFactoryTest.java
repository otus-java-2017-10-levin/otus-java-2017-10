package ru.otus.jdbc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DbManagerFactoryTest {


    @Test
    void createOnEmptyMapTest() {
        HashMap<String, String> props = new HashMap<>();
        Exception e = assertThrows(IllegalArgumentException.class, () -> DbManagerFactory.createDataBaseManager(props));
        assertEquals("Cannot create database manager with empty map", e.getMessage());
    }

    @Test
    void createTest() {
        DbManagerFactory.createDataBaseManager(JdbcTestParams.properties);
    }

    @Test
    void createDatabaseWithWrongDriverTest() {
        Map<String, String> properties = new HashMap<>();
        properties.put(JdbcTestParams.DB_DRIVER_KEY, "com.mysql.cj.jdbc.Driver1");
        Exception e = assertThrows(IllegalArgumentException.class, () -> DbManagerFactory.createDataBaseManager(properties));
        assertEquals("Driver com.mysql.cj.jdbc.Driver1 is not supported", e.getMessage());
    }
}