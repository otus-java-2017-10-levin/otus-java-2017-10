package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class EntityInstanceImpl<T> extends AbstractField<T> implements EntityInstance<T> {

    private STATE entityState = STATE.TRANSIENT;
    private List<SimpleField<?>> simpleFields = new ArrayList<>();
    private List<ArrayField<?>> arrayFields = new ArrayList<>();
    private List<EntityInstance<?>> referenceFields = new ArrayList<>();

    EntityInstanceImpl(@NotNull String name, @Nullable T value, @NotNull Class fieldClass) {
        super(name, value, fieldClass);
    }

    @Override
    public void setState(STATE currentState) {
        this.entityState = currentState;
        updateChildrenState();
    }

    private void updateChildrenState() {
        referenceFields.forEach(entity -> entity.setState(entityState));
    }


    @Override
    public STATE getState() {
        return entityState;
    }

    @NotNull
    @Override
    public List<EntityInstance<?>> getEntitiesFields() {
        return Collections.unmodifiableList(referenceFields);
    }

    @Override
    public <S> void addReferenceEntity(@NotNull EntityInstance<S> instance) {
        referenceFields.add(instance);
    }

    @Override
    public void addSimpleField(@NotNull SimpleField<?> instance) {
        simpleFields.add(instance);
    }

    @NotNull
    @Override
    public List<SimpleField<?>> getSimpleFields() {
        return Collections.unmodifiableList(simpleFields);
    }

    @Override
    public void addArrayField(@NotNull ArrayField<?> instance) {
        arrayFields.add(instance);
    }

    @NotNull
    @Override
    public List<ArrayField<?>> getArrayFields() {
        return Collections.unmodifiableList(arrayFields);
    }
}
