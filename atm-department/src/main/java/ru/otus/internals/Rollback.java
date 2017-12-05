package ru.otus.internals;

/**
 * Interface for managing object states
 */
public interface Rollback {

    /**
     * Save state for object
     */
    Memento saveState();

    /**
     * Roll to {@code memento}
     * @param memento - memento object
     */
    void loadState(Memento memento);
}
