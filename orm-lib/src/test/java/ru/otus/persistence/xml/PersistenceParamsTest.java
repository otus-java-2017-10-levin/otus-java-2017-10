package ru.otus.persistence.xml;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceParamsTest {

    private final String persistenceUnit = "otusJPAH2";
    private final String persistencePath = "persistenceTest.xml";

    @Test
    void parseXMLWithEmptyAnswer() {
        PersistenceParams result = new PersistenceParams(persistenceUnit +"1", persistencePath);
        Map<String, String> properties = result.getConnectionData();
        Set<String> classes = result.getEntityClasses();
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

        expected.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        expected.put("javax.persistence.jdbc.url", "jdbc:h2:~/test");
        expected.put("javax.persistence.jdbc.user", "sa");
        expected.put("javax.persistence.jdbc.password", "");

        assertEquals(true, expected.equals(properties));

        Set<String> classes = persistenceXml.getEntityClasses();
        Set<String> expectedClasses = new HashSet<>();

        expectedClasses.add("ru.otus.classes.UserDataSet");
        expectedClasses.add("ru.otus.classes.Address");

        assertEquals(expectedClasses, classes);
    }
}