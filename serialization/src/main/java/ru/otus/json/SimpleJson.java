package ru.otus.json;

import jdk.nashorn.api.tree.Tree;
import org.json.simple.JSONObject;
import ru.otus.common.Utils;

import java.io.*;
import java.util.*;

/**
 * Implementation of the javax.json
 */
final class SimpleJson implements SerializeBuilder {

    /**
     * Using generic based Map instead JSONObject
     */
    private final Map<String, Object> fields = new LinkedHashMap<>();

    public SerializeBuilder addObject(String name, Object value) throws IllegalArgumentException {
        Utils.throwIf(IllegalArgumentException.class, "field is already exist!", () -> fields.containsKey(name));
        fields.put(name, value);
        return this;
    }

    public SerializeBuilder addArray(String name, Object... values) throws IllegalArgumentException {
        List<Object> list = Arrays.asList(values);
        this.addObject(name, list);
        return this;
    }

    @Override
    public String toJsonString() {
        return this.toString();
    }

    @Override
    public String toString() {
        StringWriter buffer = new StringWriter();
        try {
            JSONObject.writeJSONString(fields, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
