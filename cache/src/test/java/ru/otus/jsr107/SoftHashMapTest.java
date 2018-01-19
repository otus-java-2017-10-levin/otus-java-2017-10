package ru.otus.jsr107;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SoftHashMapTest {

    private final Map<String, Integer> map = new SoftHashMap<>();
    @Test
    void putTest() {
        map.put("1", 1);

        assertEquals(new Integer(1), map.get("1"));
    }

    @Test
    void softRefTest() {
        Map<String, BigObj> objects = new SoftHashMap<>();

        for (int i=0; i< 10000; i++) {
            objects.put(""+i, new BigObj());
        }

        System.out.println(objects.size());
    }

}