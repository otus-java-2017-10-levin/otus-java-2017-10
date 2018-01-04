package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Interface keeping state of object
 * Keeps all object fields tree. It consists of:
 * {@link SimpleField} - fields of plain classes
 * {@link ArrayField} - fields of array classes
 * {@link EntityInstance} - fields of composite classes
 *
 *
 */
public interface EntityInstance<T> extends Field<T> {

    enum STATE {
        TRANSIENT,
        PERSISTENT,
        DETACHED,
        REMOVED
    }

    /**
     * Change state for all children
     * @param currentState - new state
     */
    void setState(STATE currentState);

    STATE getState();

    /**
     * @return -
     */
    @NotNull
    List<EntityInstance<?>> getEntitiesFields();

    /**
     * Add not a {@link SimpleField}
     * @param instance - child entity
     * @param <S> - type of entity
     */
    <S> void addReferenceEntity(EntityInstance<S> instance);

    void addSimpleField(@NotNull SimpleField<?> instance);

    @NotNull
    List<SimpleField<?>> getSimpleFields();

    void addArrayField(@NotNull ArrayField<?> instance);

    @NotNull
    List<ArrayField<?>> getArrayFields();
}
