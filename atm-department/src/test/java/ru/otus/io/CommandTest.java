package ru.otus.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandTest {

    @Test
     void creationTest() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new Command(null, new String[0]));
        assertEquals(null, e.getMessage());
    }

    @Test
    void gettersTest() {
        String[] arr = {"5000", "100"};
        Command cmd = new Command("add", arr);

        assertEquals("add",  cmd.getName());
        assertEquals("5000", cmd.getNext());
        assertEquals("100",  cmd.getNext());
        assertEquals(null,   cmd.getNext());
    }

    @Test
    void getLongErrorTest() {
        String[] arr = {};
        Command cmd = new Command("add", arr);

        Exception e = assertThrows(IllegalStateException.class, cmd::getLong);
        assertEquals("Wrong parameter list", e.getMessage());

        String[] arr1 = {"a"};
        Command cmd1 = new Command("add", arr1);
        assertThrows(NumberFormatException.class, cmd1::getLong);
    }

    @Test
    void getLongTest() {
        String[] arr = {"5000"};
        Command cmd = new Command("add", arr);

        assertEquals(5000L, cmd.getLong());
    }
}
