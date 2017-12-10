package ru.otus.internals;


/**
 * Common interface for working with serialization
 * 
 */
public interface JsonSerializer {

    /**
     * Converts {@code object} to json string.
     * JSON string must apply JSR 367
     * @param object - object to parse
     * @return - json string
     */
    String toJson(Object object);
}
