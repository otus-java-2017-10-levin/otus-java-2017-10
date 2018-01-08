package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class EntityStructureQueueBuilderImpl<T> implements EntityStructureQueueBuilder<T> {

    private List<T> list = new ArrayList<>();

    /**
     * Adds entity structure to queue.
     *
     * @param entityStructure - entity structure to add
     * @return - builder object
     */
    @Override
    public EntityStructureQueueBuilder<T> add(@NotNull T entityStructure) {
        list.add(entityStructure);
        return this;
    }

    /**
     * Create object EntityStructureQueue
     *
     * @return - created queue object
     */
    @Override
    public EntityStructureQueue<T> build() {
        return new EntityStructureQueueImpl(list);
    }


    private class EntityStructureQueueImpl implements EntityStructureQueue<T> {

        private List<T> list;

        private EntityStructureQueueImpl(List<T> list) {
            this.list = Collections.unmodifiableList(list);
        }

        @Override
        public void forEach(@NotNull Consumer<T> consumer) {
            list.forEach(consumer);
        }
    }

}
