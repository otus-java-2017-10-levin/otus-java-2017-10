package ru.otus.persistence.fields;

import java.util.Collection;

/**
 * Represents array or collection
 * @param <T>
 */
public interface ArrayField<T> {

    void add(T t);

    T[] getArray();

    String getName();

    Class<?> getFieldClass();
}
