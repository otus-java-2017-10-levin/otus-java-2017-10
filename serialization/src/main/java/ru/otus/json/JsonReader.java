package ru.otus.json;

public interface JsonReader {

    /**
     * * Create object from json
     * @param json - test in json format (JSR 367)
     * @param objectClass - class of the restoring object
     * @return - created object.
     */
    <T> T read(String json, Class<T> objectClass);
}
