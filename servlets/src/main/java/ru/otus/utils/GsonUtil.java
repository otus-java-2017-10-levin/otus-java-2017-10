package ru.otus.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.db.adapters.UserTypeAdapter;
import ru.otus.db.entities.User;

public class GsonUtil {

    private static Gson gson;
    private static GsonBuilder builder = new GsonBuilder();

    public static String toJson(Object object) {
        if (gson == null) {
            builder.registerTypeAdapter(User.class, new UserTypeAdapter());
            gson = builder.create();
        }
        return gson.toJson(object);
    }
}
