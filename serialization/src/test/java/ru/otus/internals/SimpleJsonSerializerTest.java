package ru.otus.internals;

import org.junit.jupiter.api.Test;
import ru.otus.json.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleJsonSerializerTest {

    private void compareWithGson(Object object) {
        JsonSerializer json = new JsonFormatter(new SimpleJsonSerializer());
        JsonSerializer gson = new JsonFormatter(JsonFactory.get(JsonFactory.JSON.GSON));
        String expected = gson.toJson(object);
        String actual = json.toJson(object);

        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void plainObjectTest() {
        compareWithGson(new Plain());
    }

    @Test
    void plainWrapperObjectTest() {
        compareWithGson(new PlainWrappers());
    }

    @Test
    void plainArraysTest() {
        compareWithGson(new PlainArrays());
    }

    @Test
    void complexArraysTest() {
        compareWithGson(new ComplexArrays());
    }

    @Test
    void plainListTest() {
        compareWithGson(new PlainList());
    }

    @Test
    void complexSetTest() {
        compareWithGson(new ComplexSet());
    }
}