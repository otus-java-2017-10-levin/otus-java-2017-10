package ru.otus.utils;

import com.google.gson.Gson;

public class GsonUtil {

    private static Gson gson;

    public static String toJson(Object object) {
        if (gson == null)
            gson = new Gson();
        return gson.toJson(object);
    }
}
