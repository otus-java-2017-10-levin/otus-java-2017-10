package ru.otus.persistence.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.persistence.AnnotationManager;
import ru.otus.persistence.annotations.AnnotatedField;

import java.lang.reflect.Array;

public class EntityInstanceBuilderImpl implements EntityInstanceBuilder {

    @Override
    @NotNull
    public <T> EntityInstance<T> build(@NotNull T object,
                                       @NotNull AnnotationManager manager) {

        EntityInstance<T> result = new EntityInstanceImpl<>("", object, object.getClass());

        try {
            addChildren(result, object, manager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    //TODO: Collections support
    private <T> void addChildren(@NotNull EntityInstance<T> result,
                                 @NotNull Object object,
                                 @NotNull AnnotationManager manager) throws IllegalAccessException {

        for (AnnotatedField f : manager.getAnnotatedClass(object.getClass()).getFields()) {
            Object value = f.getFieldValue(object);
            Class<?> fieldType = f.getType();
            AddField(f, value, manager, result);
        }
    }

    private void AddField(AnnotatedField f, @Nullable Object value, @NotNull AnnotationManager manager, EntityInstance<?> result) {
        Class<?> fieldType = f.getType();
        if (Field.isSimpleField(fieldType)) {
            result.addSimpleField(createSimpleField(f.getName(), value));
        } else if (Field.isArrayField(fieldType)) {
            result.addArrayField(addArrayField(f.getName(), (Object[]) value));
        } else {
            if (value != null && manager.contains(value.getClass()))
                result.addReferenceEntity(createRefEntity(value, manager));
            else {
                System.out.println("skipping class: " + fieldType);
            }
        }
    }

    private <T> SimpleField<T> createSimpleField(String name, T value) {
        return new SimpleFieldImpl<>(name, value, value.getClass());
    }

    private <T> EntityInstance<T> createRefEntity(@NotNull T value, @NotNull AnnotationManager manager) {
        return build(value, manager);
    }


    private <T> ArrayField<T> addArrayField(String name, T[] value) {
        return new ArrayFieldImpl<>(name, value);
    }
}
