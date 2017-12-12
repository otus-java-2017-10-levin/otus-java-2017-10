package ru.otus.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

class GsonSerializer implements JsonSerializer {

    private final Gson gson = new Gson();

    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T fromJson(String jsonString, Class<T> objectClass) {
        return gson.fromJson(jsonString, objectClass);
    }

    public static String formatJson(String string) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(string);
        return gson.toJson(je);
    }
}
