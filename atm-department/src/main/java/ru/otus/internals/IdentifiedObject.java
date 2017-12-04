package ru.otus.internals;


public interface IdentifiedObject<T> {

    /**
     * Return unique id for object
     * @return object id
     */
    T getId();
}
