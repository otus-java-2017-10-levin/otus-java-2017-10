package ru.otus.internals;

public interface JsonDeserializer {
    /**
     * Convert {@code jsonString} to object of {@code objectClass}
     * @param jsonString - string to parse
     * @return - created object with json fields
     */
    <T> T fromJson(String jsonString, Class<T> objectClass);
}
