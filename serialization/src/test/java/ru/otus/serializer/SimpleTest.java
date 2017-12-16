package ru.otus.serializer;

import org.junit.jupiter.api.Test;
import ru.otus.classes.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTest {
    public static void checkJson(Object obj, Class<?> cl, JsonFactory.JSON type) {
        JsonSerializer json = JsonFactory.get(type);
//        JsonSerializer json = new JsonFormatter(JsonFactory.get(type));
        JsonSerializer gson = JsonFactory.get(JsonFactory.JSON.GSON);
//        JsonSerializer gson = new JsonFormatter(JsonFactory.get(JsonFactory.JSON.GSON));

        String jsonString = json.toJson(obj);
        System.out.println(jsonString);
        Object obj1 = gson.fromJson(jsonString, cl);
        assertEquals(true, obj.equals(cl.cast(obj1)));
    }

    @Test
    void plainTest() {
        Plain a = new Plain();
        a.init();
        checkJson(a, Plain.class, JsonFactory.JSON.SIMPLE);
    }

    @Test
    void plainEmptyTest() {
        Plain a = new Plain();
        checkJson(a, Plain.class, JsonFactory.JSON.SIMPLE);
    }

    @Test
    void plainArraysTest() {
        PlainArrays a = new PlainArrays();
        a.init();
        checkJson(a, PlainArrays.class, JsonFactory.JSON.SIMPLE);
    }

    @Test
    void complexSetTest() {
        ComplexSet a = new ComplexSet();
        a.init();
        checkJson(a, ComplexSet.class, JsonFactory.JSON.SIMPLE);
    }

    @Test
    void plainListTest() {
        PlainList a = new PlainList();
        a.init();
        checkJson(a, PlainList.class, JsonFactory.JSON.SIMPLE);
    }


    @Test
    void plainWrappers() {
        PlainWrappers a = new PlainWrappers();
        a.init();
        checkJson(a, PlainWrappers.class, JsonFactory.JSON.SIMPLE);
    }
}