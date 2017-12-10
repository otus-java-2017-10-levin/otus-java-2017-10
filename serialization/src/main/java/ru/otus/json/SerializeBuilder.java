package ru.otus.json;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Builder interface to write object field to string.
 * We need to know how to write:
 * 1. Primitive types and Wrappers
 * 2. Arrays
 * 3. Strings
 *
 * Everything else are assumed as complex object, and need to split.
 *
 */
public interface SerializeBuilder {

    /**
     * Add pair {@code name: value} to builder.
     * If builder already contains {@code name} throw an exception.
     * @param name - name of the serialized field
     * @param value - value of thr serialized field
     * @throws IllegalArgumentException - throws IllegalArgumentException when name already exist in this builder.
     * @return - builder object
     */
    SerializeBuilder addObject(String name, FieldEntity value) throws IllegalArgumentException;

    /**
     * Add pair {@code name: values[]} to builder.
     * If builder already contains {@code name} throw an exception.
     * @param name - name of the serialized field
     * @param values - value of thr serialized field
     * @throws IllegalArgumentException - throws IllegalArgumentException when name already exist in this builder.
     * @return - builder object
     */
    SerializeBuilder addArray(String name, Object... values) throws IllegalArgumentException;



    SerializeBuilder addMap(Map<String, FieldEntity> map);


    /**
     * Serialize Builder to String object.
     * @return - serialized string
     */
    String toJsonString();
}