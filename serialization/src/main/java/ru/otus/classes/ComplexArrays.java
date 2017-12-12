package ru.otus.classes;

import com.google.gson.*;
import lombok.Data;
import ru.otus.serializer.JsonSerializer;

@SuppressWarnings("ALL")
@Data
public class ComplexArrays {
    @SuppressWarnings("unused")
    private int anInt;
    @SuppressWarnings("unused")
    private Object[] objects;

    public void init() {
        anInt = 10;
        objects = new Object[]{new Plain(), new PlainArrays(), 3L};
    }

    public void loadArray(JsonSerializer gson, String json) {
        objects = new Object[3];
        JsonParser parser = new JsonParser();
        JsonElement tree = parser.parse(json);
        JsonObject jsonObject = tree.getAsJsonObject();
        JsonElement array = jsonObject.get("objects");
        if (array.isJsonArray()) {
            JsonArray arr = array.getAsJsonArray();
            objects[0] = gson.fromJson(arr.get(0).toString(), Plain.class);
            objects[1] = gson.fromJson(arr.get(1).toString(), PlainArrays.class);
            objects[2] = gson.fromJson(arr.get(2).toString(), long.class);
        }
    }
}