package ru.otus.internals;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.json.*;
import java.lang.reflect.Field;
import java.util.*;


final class JavaXJson implements JsonSerializer {
    private final JsonBuilderFactory factory = Json.createBuilderFactory(null);

    public String toJson(Object object) {
        JsonObject objectMap = null;
        try {
            objectMap = parseObject(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assert objectMap != null;
        return objectMap.toString();
    }

    private JsonObject parseObject(Object object) throws IllegalAccessException {
        if (object == null)
            return null;
        JsonObjectBuilder builder = factory.createObjectBuilder();

        for (Field f : FieldUtils.getAllFields(object.getClass())) {
            String fieldName = f.getName();
            Class<?> fieldClass = f.getType();
            Object value = FieldUtils.readDeclaredField(object, fieldName, true);

            if (value == null)
                builder.addNull(fieldName);

            if (isCollection(fieldClass)) {
                Collection<?> coll = Collection.class.cast(value);
                assert coll != null;
                builder.add(fieldName, putArray((coll.toArray())));
            } else if (isPlain(fieldClass)) {
                builder.add(fieldName, JavaXJson.valueOf(value));
            } else if (fieldClass.isArray()) {
                assert value != null;
                builder.add(fieldName, putArray((Object[]) value));
            } else {
                assert value != null;
                builder.add(fieldName, parseObject(value));
            }
        }
        return builder.build();
    }

    private JsonArray putArray(Object[] list) throws IllegalAccessException {

        JsonArrayBuilder builder = Json.createArrayBuilder();

        for (Object obj : list) {
            if (obj == null) {
                builder.addNull();
                continue;
            }
            if (isPlain(obj.getClass())) {
                builder.add(JavaXJson.valueOf(obj));
            } else {
                builder.add(parseObject(obj));
            }
        }
        return builder.build();
    }

    private boolean isCollection(Class<?> cl) {
        return Collection.class.isAssignableFrom(cl);
    }

    private boolean isPlain(Class<?> cl) {
        return ClassUtils.isPrimitiveOrWrapper(cl) || cl.equals(String.class);
    }

    private static JsonValue valueOf(Object object) {

        if (object == null)
            return JsonValue.NULL;

        if (object instanceof Number) {
            return JavaXJson.toNumber(object);
        }
        if (object.getClass().equals(Character.class)) {
            Character value = (Character) object;
            return Json.createValue(value.toString());
        }

        if (object.getClass().equals(Boolean.class)) {
            boolean b = (Boolean)object;
            return b ? JsonValue.TRUE : JsonValue.FALSE;
        }
        if (object.getClass().equals(String.class)) {
            return Json.createValue((String)object);
        }
        return JsonValue.NULL;
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