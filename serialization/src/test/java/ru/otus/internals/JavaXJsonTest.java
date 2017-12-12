package ru.otus.internals;

import org.junit.jupiter.api.Test;
import ru.otus.classes.*;

import static org.junit.jupiter.api.Assertions.*;

class JavaXJsonTest {

    private void compareWithGson(Object object) {
        JsonSerializer json = new JsonFormatter(new JavaXJson());
        JsonSerializer gson = new JsonFormatter(JsonFactory.get(JsonFactory.JSON.GSON));
        String expected = gson.toJson(object);
        String actual = json.toJson(object);

        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void plainTest() {
        compareWithGson(new Plain());
    }

    @Test
    void plainArraysTest() {
        compareWithGson(new PlainArrays());
    }

    @Test
    void complexSetTest() {
        compareWithGson(new ComplexSet());
    }

    @Test
    void plainListTest() {
        compareWithGson(new PlainList());
    }

    @Test
    void complexArrayTest() {
        compareWithGson(new ComplexArrays());
    }

    @Test
    void plainWrappers() {
        compareWithGson(new PlainWrappers());
    }
}