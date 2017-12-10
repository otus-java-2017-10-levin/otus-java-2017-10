package ru.otus.internals;

import java.util.HashMap;
import java.util.Map;

public class JsonFactory {
    private JsonFactory() {}

    public enum JSON {
        GSON,
        SIMPLE
    }

    public static JsonSerializer get(JSON json) {
        return jsonSerializer.getOrDefault(json, null);
    }

    private static final Map<JSON, JsonSerializer> jsonSerializer = new HashMap<JSON, JsonSerializer>();
    static {
        jsonSerializer.put(JSON.GSON, new GsonSerializer());
    }
}
