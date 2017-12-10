package ru.otus.internals;

abstract class JsonDecorator implements JsonSerializer {
    private JsonSerializer serializer;

    public JsonDecorator(JsonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String toJson(Object object) {
        return serializer.toJson(object);
    }
}
