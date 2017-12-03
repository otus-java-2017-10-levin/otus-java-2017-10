package ru.otus.internals;

/**
 * Interface for managing object states
 */
public interface Rollback {

    /**
     * States for rollback.
     * INITIAL STATE - first saved state
     * LAST_MODIFICATION - last modified state
     */
    enum STATES {
        INITIAL,
        LAST_MODIFICATION
    }
    /**
     * Save state for object
     */
    void saveState();

    /**
     * Roll to {@code currentState}
     * We can rollback to the first save state, or rollforward to the last saved state
     * @param currentState - one of the states from {@code STATE}
     */
    void loadState(STATES currentState);
}
