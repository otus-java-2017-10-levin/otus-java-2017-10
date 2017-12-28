package ru.otus.xml;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceParamsTest {

    private final String persistenceUnit = "otusJPAMySQL";
    private final String persistencePath = "META-INF/persistence.xml";

    @Test
    void parseXMLWithNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new PersistenceParams(null, ""));
        assertEquals("Argument is null", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> new PersistenceParams(persistenceUnit, null));
        assertEquals("Argument is null", e.getMessage());
    }

    @Test
    void parseXMLWithEmptyAnswer() {
        PersistenceParams result = new PersistenceParams(persistenceUnit +"1", persistencePath);
        Map<String, String> properties = (Map<String, String>)result.getConnectionData();
        Set<String> classes = (Set<String>)result.getEntityClasses();
        assertEquals(0, properties.size());
        assertEquals(0, classes.size());
    }

    @Test
    void parseXMLWrongURL() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new PersistenceParams(persistenceUnit, ""));
        assertEquals("Wrong XML path!", e.getMessage());
    }


    @Test
    void parseValidXML() {
        PersistenceParams persistenceXml = new PersistenceParams(persistenceUnit, persistencePath);
        Map<String, String> properties = persistenceXml.getConnectionData();
        Map<String, String> expected = new HashMap<>();

        expected.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        expected.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/db_example");
        expected.put("javax.persistence.jdbc.user", "Flow");
        expected.put("javax.persistence.jdbc.password", "grandmaster");

        assertEquals(true, expected.equals(properties));

        Set<String> classes = persistenceXml.getEntityClasses();
        Set<String> expectedClasses = new HashSet<>();

        expectedClasses.add("ru.otus.base.UsersDataSet");
        expectedClasses.add("ru.otus.base.UsersDataSet1");

        assertEquals(expectedClasses, classes);
    }

}