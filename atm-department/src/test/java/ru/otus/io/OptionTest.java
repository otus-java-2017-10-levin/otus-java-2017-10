package ru.otus.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptionTest {
    @Test
    void createTest() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new Option(null, true, ""));
        assertEquals(null, e.getMessage());

        new Option("get", false, "");
        e = assertThrows(IllegalArgumentException.class, () -> new Option("", true, ""));
        assertEquals(null, e.getMessage());
    }

    @Test
    void gettersTest() {
        Option opt = new Option("get", true, "test");

        assertEquals("get", opt.getName());
        assertEquals(true, opt.getHasArgs());
        assertEquals("test", opt.getDescription());
    }
}
