package ru.otus.json;

import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

final class SimpleObjectBuilder implements ObjectBuilder {
    private final Map<String, Object> object = new LinkedHashMap<>();

    @Override
    public ObjectBuilder addNode(String name, Object node) {
        object.put(name, node);
        return this;
    }

    @Override
    public Object build() {
        return new JSONObject(object);
    }
}
