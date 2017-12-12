package ru.otus.json;

import javax.json.*;

final class JavaXUtils {
    private JavaXUtils() {}

    public static JsonValue valueOf(Object object) {

        if (object == null)
            return JsonValue.NULL;

        if (object instanceof Number) {
            return toNumber(object);
        }
        if (object instanceof JsonObject ||
                object instanceof JsonArray) {
            return (JsonValue)object;
        }
        if (object.getClass().equals(Character.class)) {
            Character value = (Character) object;
            return Json.createValue(value.toString());
        }

        if (object.getClass().equals(Boolean.class)) {
            boolean b = (Boolean) object;
            return b ? JsonValue.TRUE : JsonValue.FALSE;
        }
        if (object.getClass().equals(String.class)) {
            return Json.createValue((String) object);
        }
        throw new IllegalArgumentException("not a plain object");
    }

    private static JsonNumber toNumber(Object obj) {
        Class<?> cl = obj.getClass();

        if (cl.equals(Float.class)) {
            return Json.createValue((Float) obj);
        }
        if (cl.equals(Double.class)) {
            return Json.createValue((Double) obj);
        }
        if (cl.equals(Byte.class)) {
            return Json.createValue((Byte) obj);
        }
        if (cl.equals(Short.class)) {
            return Json.createValue((Short) (obj));
        }
        if (cl.equals(Integer.class)) {
            return Json.createValue((Integer) obj);
        }
        if (cl.equals(Long.class)) {
            return Json.createValue((Long) obj);
        }
        throw new IllegalArgumentException("is not a number");
    }
}
