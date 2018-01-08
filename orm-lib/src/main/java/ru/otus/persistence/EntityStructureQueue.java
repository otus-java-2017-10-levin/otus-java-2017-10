package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Interface to hold order in which we write objects to DB.
 * Represent a unmodifiable FIFO queue. Each object in queue
 * represents structure of an entity, that should be written in DB.
 *
 * Creation of interface implementation has to be with builder pattern.
 * Once the object is created, we cannot add elements to it.
 *
 * @param <T> - type of entity structure representation
 */

public interface EntityStructureQueue<T> {

    void forEach(@NotNull Consumer<T> consumer);
}
