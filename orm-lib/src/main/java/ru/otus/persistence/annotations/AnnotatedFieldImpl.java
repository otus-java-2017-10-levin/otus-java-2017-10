package ru.otus.persistence.annotations;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

final class AnnotatedFieldImpl implements AnnotatedField {
    private final Field field;
    private final String name;
    private final AnnotatedClass annotatedClass;
    private final Class<?> componentType;


    private final Map<Class<? extends Annotation>, ? extends Annotation> annotations;

    AnnotatedFieldImpl(@NotNull Field field, @NotNull AnnotatedClass cl) {
        annotations = Arrays.stream(field.getDeclaredAnnotations())
                .collect(Collectors.toMap(Annotation::annotationType, annotation -> annotation));
        this.field = field;
        this.name = field.getName();
        this.annotatedClass = cl;

        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType par = (ParameterizedType) type;
            Type[] fieldArgTypes = par.getActualTypeArguments();
            if (fieldArgTypes.length == 0)
                throw new IllegalStateException();

            componentType = (Class) fieldArgTypes[0];
        } else
            componentType = null;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public String getFullName(String separator) {
        return this.annotatedClass.getSimpleName()+separator+this.name;
    }
    @NotNull
    @Override
    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public Class<?> getComponentType() {
        return this.componentType;
    }

    @Override
    public boolean contains(@NotNull Class<? extends Annotation> annotation) {
        return annotations.containsKey(annotation);
    }

    @Override
    public int getAnnotationCount() {
        return annotations.size();
    }

    @Nullable
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotationClass.cast(annotations.get(annotationClass));
    }

    @Override
    @Nullable
    public Object getFieldValue(@NotNull Object object) throws IllegalAccessException {
        return FieldUtils.readField(field, object, true);
    }

    @Override
    public void setFieldValue(@NotNull Object target, @NotNull Object value) throws IllegalAccessException {
        FieldUtils.writeField(field, target, value, true);
    }

    @Override
    public String toString() {
        return "AnnotatedFieldImpl{" +
                "field=" + field +
                ", annotations=" + annotations +
                '}';
    }
}