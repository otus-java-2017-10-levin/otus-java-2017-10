package ru.otus.jdbc;

import org.junit.jupiter.api.Test;
import ru.otus.xml.PersistanceParser;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DbManagerFactoryTest {

    private static final String persistanceUnit = "otusJPA";
    private static final String persistancePath = "META-INF/persistence.xml";

    public static final Map<String, String> properties = PersistanceParser.parse(persistanceUnit, persistancePath);
    @Test
    void createOnEmptyMapTest() {
        HashMap<String, String> props = new HashMap<>();
        Exception e = assertThrows(IllegalArgumentException.class, () -> DbManagerFactory.createDataBaseManager(props));
        assertEquals("Cannot create database manager with empty map", e.getMessage());
    }

    @Test
    void createTest() {
        DbManager dbManager = DbManagerFactory.createDataBaseManager(properties);

        assertEquals(true, dbManager != null);
    }

    @Test
    void createDatabaseWithWrongDriverTest() {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver1");
        Exception e = assertThrows(IllegalArgumentException.class, () -> DbManagerFactory.createDataBaseManager(properties));
        assertEquals("Cannot create database manager. Unsupported driver.", e.getMessage());
    }
}