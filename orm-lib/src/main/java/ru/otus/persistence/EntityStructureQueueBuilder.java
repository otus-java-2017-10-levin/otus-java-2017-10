package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;

public interface EntityStructureQueueBuilder<T> {

    /**
     * Adds entity structure to queue.
     * @param entityStructure - entity structure to add
     * @return - builder object
     */
    EntityStructureQueueBuilder<T> add(@NotNull T entityStructure);


    /**
     * Create object EntityStructureQueue
     * @return - created queue object
     */
    EntityStructureQueue<T> build();
}
