package ru.otus.serializer;

abstract class JsonDecorator implements JsonSerializer {
    private final JsonSerializer serializer;

    JsonDecorator(JsonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String toJson(Object object) {
        return serializer.toJson(object);
    }

    @Override
    public <T> T fromJson(String jsonString, Class<T> objectClass) {
        return serializer.fromJson(jsonString, objectClass);
    }
}
