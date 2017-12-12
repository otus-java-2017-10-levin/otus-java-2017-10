package ru.otus.serializer;

import java.util.HashMap;
import java.util.Map;

public class JsonFactory {
    private JsonFactory() {}

    public enum JSON {
        GSON,
        JAVAX,
        SIMPLE
    }

    public static JsonSerializer get(JSON json) {
        return jsonSerializer.getOrDefault(json, null);
    }

    private static final Map<JSON, JsonSerializer> jsonSerializer = new HashMap<>();
    static {
        jsonSerializer.put(JSON.GSON, new GsonSerializer());
        jsonSerializer.put(JSON.JAVAX, new JavaXSerializer());
        jsonSerializer.put(JSON.SIMPLE, new SimpleSerializer());
    }
}