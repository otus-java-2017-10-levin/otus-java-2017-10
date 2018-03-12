package ru.otus.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.db.adapters.UserTypeAdapter;
import ru.otus.db.entities.User;

public class GsonUtil {

    private static final GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    public static String toJson(Object object) {
        if (gson == null) {
            builder.registerTypeAdapter(User.class, new UserTypeAdapter());
            gson = builder.create();
        }
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> cl) {
        if (gson == null) {
            builder.registerTypeAdapter(User.class, new UserTypeAdapter());
            gson = builder.create();
        }
        return gson.fromJson(json, cl);
    }
}