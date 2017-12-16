package ru.otus.serializer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.otus.json.ArrayBuilder;
import ru.otus.json.BuilderFactory;
import ru.otus.json.ObjectBuilder;

import java.lang.reflect.Field;
import java.util.*;

abstract class AbstractSerializer implements JsonSerializer {
    private final BuilderFactory.Builder builder;

    AbstractSerializer(BuilderFactory.Builder builder) {
        this.builder = builder;
    }

    @Override
    public String toJson(Object object) {
        if (object.getClass().isArray()) {
            try {
                return BuilderFactory.createArrayBuilder(this.builder).add(putArray(convert(object))).build().toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            return parseObject(object).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public <T> T fromJson(String jsonString, Class<T> objectClass) {
        throw new UnsupportedOperationException();
    }

    private Object parseObject(Object object) throws IllegalAccessException {
        if (object == null)
            return null;
        ObjectBuilder builder = BuilderFactory.createBuilder(this.builder);

        for (Field f : FieldUtils.getAllFields(object.getClass())) {
            String fieldName = f.getName();
            Class<?> fieldClass = f.getType();
            Object value = FieldUtils.readDeclaredField(object, fieldName, true);

            if (value == null)
                builder.addNode(fieldName, null);

            if (isCollection(fieldClass)) {

                if (value != null) {
                    Collection<?> coll = Collection.class.cast(value);
                    builder.addNode(fieldName, putArray(coll.toArray()));
                }
            } else if (isPlain(fieldClass)) {
                builder.addNode(fieldName, value);
            } else if (fieldClass.isArray()) {
                if (value != null) {
                    Class arrayClass = value.getClass().getComponentType();
                    if (arrayClass.isPrimitive()) {
                        builder.addNode(fieldName, putArray(convert(value)));
                    } else {
                        builder.addNode(fieldName, putArray((Object[]) value));
                    }
                }
            } else {
                assert value != null;
                builder.addNode(fieldName, parseObject(value));
            }
        }
        return builder.build();
    }

    private Object[] convert(Object primitiveArray) {
        Class<?> cl = primitiveArray.getClass();
        if (cl.equals(boolean[].class)) {
            return ArrayUtils.toObject(boolean[].class.cast(primitiveArray));
        }
        if (cl.equals(byte[].class)) {
            return ArrayUtils.toObject(byte[].class.cast(primitiveArray));
        }
        if (cl.equals(char[].class)) {
            return ArrayUtils.toObject(char[].class.cast(primitiveArray));
        }
        if (cl.equals(short[].class)) {
            return ArrayUtils.toObject(short[].class.cast(primitiveArray));
        }
        if (cl.equals(int[].class)) {
            return ArrayUtils.toObject(int[].class.cast(primitiveArray));
        }
        if (cl.equals(long[].class)) {
            return ArrayUtils.toObject(long[].class.cast(primitiveArray));
        }
        if (cl.equals(float[].class)) {
            return ArrayUtils.toObject(float[].class.cast(primitiveArray));
        }
        if (cl.equals(double[].class)) {
            return ArrayUtils.toObject(double[].class.cast(primitiveArray));
        }
        throw new IllegalStateException();
    }

    private Object putArray(Object[] list) throws IllegalAccessException {
        ArrayBuilder builder = BuilderFactory.createArrayBuilder(this.builder);

        if (list == null)
            return null;
        for (Object obj : list) {
            if (obj == null) {
                builder.add(null);
                continue;
            }
            if (isPlain(obj.getClass())) {
                builder.add(obj);
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
}