package ru.otus.io;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleParserTest
{
    @Test
    void addOptionTest() {
        Parser parser = new SimpleParser();

        Exception e = assertThrows(IllegalArgumentException.class, () ->parser.addOption(null, false, "descr"));
        assertEquals(null ,e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () ->parser.addOption("", false, "descr"));
        assertEquals(null ,e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () ->parser.addOption("get", false, null));
        assertEquals(null ,e.getMessage());

        parser.addOption("get", false, "");
        e = assertThrows(IllegalArgumentException.class, () ->parser.addOption("get", false, ""));
        assertEquals(null ,e.getMessage());
    }

    @Test
    void hasOptionTest() {
        Parser parser = new SimpleParser();

        parser.addOption("get", true, "descr");

        assertEquals(true, parser.hasOption("get"));
        assertEquals(false, parser.hasOption("add"));
    }

    @Test
    void parseErrorTest() {
        Parser parser = new SimpleParser();
        String cmd = "get 5000";
        Command command = new Command();

        Exception e = assertThrows(IllegalArgumentException.class, () ->parser.parse(cmd, command));
        assertEquals("Wrong command", e.getMessage());

        parser.addOption("get", false, "");
        parser.addOption("add", true, "");

        e = assertThrows(IllegalArgumentException.class, () ->parser.parse(cmd, command));
        assertEquals("Wrong parameter list", e.getMessage());

        String cmd1 = "add";
        e = assertThrows(IllegalArgumentException.class, () ->parser.parse(cmd1, command));
        assertEquals("Wrong parameter list", e.getMessage());
    }

    @Test
    void parseTest() {
        Parser parser = new SimpleParser();
        parser.addOption("get", true, "");
        String cmd = "get 5000";
        Command comm = new Command();
        parser.parse(cmd, comm);

        assertEquals("get", comm.getName());
        assertEquals("5000", comm.getNext());
        assertEquals(null, comm.getNext());
    }
}
