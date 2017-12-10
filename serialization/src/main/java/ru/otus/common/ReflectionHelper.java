package ru.otus.common;

import java.lang.reflect.Field;

public class ReflectionHelper {

    public static Object getValue(Field field, Object object) {
        Object res = null;
        boolean isAccessible = field.canAccess(object);
        try {
            field.setAccessible(true);
            res = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        field.setAccessible(isAccessible);
        return res;
    }

    public static Field[] getFields(Class<?> cl) {
        return cl.getDeclaredFields();
    }
}
