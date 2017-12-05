package ru.otus.internals;

/**
 * Interface for managing object states
 */
interface Rollback<T> {

    /**
     * Save state for object
     */
    Memento<T> saveState();

    /**
     * Roll to {@code memento}
     * @param memento - memento object
     */
    void loadState(Memento<T> memento);
}
