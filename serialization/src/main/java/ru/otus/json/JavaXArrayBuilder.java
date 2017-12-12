package ru.otus.json;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;

final class JavaXArrayBuilder implements ArrayBuilder {
    private final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    private JsonArrayBuilder instance;

    @Override
    public ArrayBuilder add(Object object) {
        checkInstance();
        try {
            instance.add(JavaXUtils.valueOf(object));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Object build() {
        checkInstance();
        return instance.build();
    }

    private void checkInstance() {
        if (instance == null) {
            instance = factory.createArrayBuilder();
        }
    }
}
