package ru.otus.persistence;

import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;

import java.lang.reflect.InvocationTargetException;

/**
 * Builder for reconstructing objects from database
 * We set fields values through getters via reflection, and call build.

 * @param <T> - object's class
 */
final class ObjectBuilder<T> {
    private T object;

    ObjectBuilder(Class<? extends T> cl) {
        try {
            object = cl.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set {@code value} for the field {@code fieldName}
     * @param fieldName - string name of field
     * @param value - new value for the {@code fieldName}
     * @param <R> -
     * @return -
     */
    public <R> ObjectBuilder<T> set(String fieldName, R value) {
        try {
            AnnotatedField field = AnnotatedClass.of(object.getClass()).getField(fieldName);
            PersistenceHelper.setFieldValue(object, field, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this;
    }

    public T build() {
        return object;
    }
}
