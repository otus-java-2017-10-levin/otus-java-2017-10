package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class to handle entities. It is a layer between EntityManager and DataBase
 */

public interface Persister {

    /**
     * Save or update Object.
     * @param object - object to save
     * @return object id
     */

    long save(@NotNull Object object);

    void updateKeys(@NotNull Object object) throws IllegalAccessException;

    /**
     *  Find object by {@code entityClass} any it's primary key.
     *  If composite key is used, for find you should use @Embedded id class
     * @param entityClass - class of entity to find
     * @param primaryKey - primary key object
     * @param <T> -
     * @return - founded object or null in nothing has found
     */
    @Nullable
    <T> T find(@NotNull Class<T> entityClass,
               long primaryKey);
}