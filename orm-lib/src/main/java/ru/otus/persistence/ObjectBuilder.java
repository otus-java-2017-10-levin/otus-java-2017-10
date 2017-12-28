package ru.otus.persistence;

import org.apache.commons.lang3.reflect.MethodUtils;
import ru.otus.base.UsersDataSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Builder for reconstructing objects from database
 *
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

    public <R> ObjectBuilder<T> set(String fieldName, R value) {
        try {
            PersistenceHelper.setFieldValue(object, fieldName, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this;
    }

    public T build() {
        return object;
    }
}
