package ru.otus.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleJsonTest {

    @Test
    void addObject() {
        SimpleJson json = new SimpleJson();

        json.addObject("name", "text");
        json.addObject("int", 10);
//        json.addObject("boolean", true);
//        json.addObject("float", 10.5f);

        String res = json.toString();
        System.out.println(res);
        assertEquals("{\"name\":\"text\",\"int\":10}", res);
    }

    @Test
    void addArray() {
        SimpleJson json = new SimpleJson();
        SimpleJson json1 = new SimpleJson();


        json.addObject("name", "text");
        json.addObject("int", 10);
        json1.addObject("testArr", 23);
        json.addArray("list", json1,2,3);

        String res = json.toString();
        System.out.println(res);
    }
}