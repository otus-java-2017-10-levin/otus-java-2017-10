package ru.otus.json;

/**
 * Common interface for communication with concrete json parser library and AbstractSerializer class
 *
 */
public interface ObjectBuilder {

    /**
     *  adding plain object to json tree
     *  Plain object is:
     *  primitives/wrappers and String class
     * @param name - name of field
     * @param node - plain object
     * @return - builder object
     */
    @SuppressWarnings("UnusedReturnValue")
    ObjectBuilder addNode(String name, Object node);

    /**
     * Finish constructing
     * @return - json object
     */
    Object build();
}
