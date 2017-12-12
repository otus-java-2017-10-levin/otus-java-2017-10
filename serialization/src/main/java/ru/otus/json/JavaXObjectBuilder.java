package ru.otus.json;

import javax.json.*;

final class JavaXObjectBuilder implements ObjectBuilder {
    private final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    private javax.json.JsonObjectBuilder instance;

    @Override
    public ObjectBuilder addNode(String name, Object node) {
        if (instance == null) {
            instance = factory.createObjectBuilder();
        }
        try {
            instance.add(name, JavaXUtils.valueOf(node));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Object build() {
        return instance.build();
    }
}