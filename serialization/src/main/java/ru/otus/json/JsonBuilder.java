package ru.otus.json;

import java.util.HashMap;
import java.util.Map;

public class JsonBuilder {

    public enum JSON {
        JAVAX,
        SIMPLE
    }
    private JsonBuilder() {}

    public static SerializeBuilder getBuilder(JSON json) {
//        if (json == JSON.SIMPLE)
        return new SimpleJson();
    }

}
