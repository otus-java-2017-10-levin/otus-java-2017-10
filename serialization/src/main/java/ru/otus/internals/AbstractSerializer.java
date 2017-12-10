package ru.otus.internals;

import org.apache.commons.lang3.ClassUtils;
import ru.otus.common.ReflectionHelper;
import ru.otus.common.Utils;
import ru.otus.json.FieldEntity;
import ru.otus.json.SerializeBuilder;

import java.lang.reflect.Field;
import java.util.*;


abstract class AbstractSerializer implements JsonSerializer {
    protected final SerializeBuilder builder;

    AbstractSerializer(SerializeBuilder builder) {
        this.builder = builder;
    }

    @Override
    public String toJson(Object object) {
        Utils.throwIf(IllegalArgumentException.class, "object = null", () -> object == null);
        build(object);
        return builder.toString();
    }

    protected abstract void addMapToBuilder(Map<String, Object> map);

    private void build(Object obj) {
        Map<String, Object> objectMap = parseObject(obj);

        addMapToBuilder(objectMap);
    }

    /**
     * Function get all fields of the object. Iterates through it.
     * If field is one of the primitive type or String simply add it to the builder
     * <p>
     * If field is an array - add it to a builder.
     * <p>
     * If field is a Collection - function iterates through it and checks every field.
     * Note that order in collection may be significant.
     * Possible cases:
     * 1. Collection is primitive or String array.
     * Function add collection to builder.
     * 2. Collection is array of complex objects.
     * Function iterates through collection and add complex map to builder.
     * 3. Combination of 1 and 2.
     *
     * @param object
     * @return
     */
    private Map<String, Object> parseObject(Object object) {
        if (object == null)
            return null;
        Map<String, Object> fieldMap = new LinkedHashMap<>();

        for (Field f : ReflectionHelper.getFields(object.getClass())) {
            FieldEntity entity = new FieldEntity(f, object);

            if (entity.getValue() == null)
                continue;

            if (fieldClass == Character.class || fieldClass == char.class) {
                fieldMap.put(fieldName, String.format("%c", (char)value));
            } else if (isCollection(fieldClass)) {
                Collection<?> coll = Collection.class.cast(value);
                putArray(fieldMap, fieldName, (coll.toArray() ));
            } else if (isPlain(fieldClass)) {
                fieldMap.put(fieldName, value);

            } else if (fieldClass.isArray()) {
                putArray(fieldMap, fieldName, (Object[])value);
            } else {
                putObject(fieldMap, fieldName, value);
            }
        }
        return fieldMap;
    }

    private void putObject(Map<String, Object> map, String name, Object object) {
        Utils.throwIf(IllegalArgumentException.class, "Input params is null",
                () -> map == null || name == null || object == null);

        Utils.throwIf(IllegalArgumentException.class, "Field " + name + "already exist in map", () -> map.containsKey(name));
        map.put(name, object);
    }

    private void putArray(Map<String, Object> map, String name, Object[] list) {
        List<Object> array = new ArrayList<>();

        for (Object obj : list) {
            if (obj == null) {
                array.add(null);
                continue;
            }
            if (isPlain(obj.getClass())) {
                array.add(obj);
            } else {
                array.add(parseObject(obj));
            }
        }
        map.put(name, array);
    }

    private boolean isPlain(Class<?> objectClass) {
        return ClassUtils.isPrimitiveOrWrapper(objectClass) || objectClass == String.class;
    }

    private boolean isCollection(Class<?> objectClass) {
        return Collection.class.isAssignableFrom(objectClass);
    }
}