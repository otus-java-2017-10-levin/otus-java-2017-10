package ru.otus.xml;

import jdk.nashorn.internal.runtime.ECMAException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersistanceParserTest {

    private String persistanceUnit = "otusJPA";
    private String persistancePath = "META-INF/persistence.xml";

    @Test
    void parseXMLWithNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            PersistanceParser.parse(null, "");
        });
        assertEquals("Argument is null", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> {
            PersistanceParser.parse(persistanceUnit, null);
        });
        assertEquals("Argument is null", e.getMessage());
    }

    @Test
    void parseXMLWithEmptyAnswer() {
        Map<String, String> result = PersistanceParser.parse(persistanceUnit+"1", persistancePath);
        assertEquals(0, result.size());
    }

    @Test
    void parseXMLWrongURL() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            PersistanceParser.parse(persistanceUnit, "");
        });
        assertEquals("Wrong XML path!", e.getMessage());
    }


    @Test
    void parseValidXML() {
        Map<String, String> props = PersistanceParser.parse(persistanceUnit, persistancePath);
        Map<String, String> expected = new HashMap<>();

        expected.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        expected.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/db_example");
        expected.put("javax.persistence.jdbc.user", "Flow");
        expected.put("javax.persistence.jdbc.password", "grandmaster");

        assertEquals(true, expected.equals(props));
    }

}