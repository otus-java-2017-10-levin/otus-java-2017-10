package ru.otus.persistence.fields;

/**
 * Represents array or collection
 * @param <T>
 */
interface ArrayField<T> {

    void add(T t);

    T[] getArray();

    String getName();

    Class<?> getFieldClass();
}
