package ru.otus.annotations;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class AnnotatedClass {

    private static Map<Class<?>, AnnotatedClass> cacheClasses;
    private final Class<?> annotatedClass;

    public Class<?> getAnnotatedClass() {
        return annotatedClass;
    }

    private final Set<Field> fields = new HashSet<>();
    private Field idField;

    private AnnotatedClass(Class<?> annotatedClass) {
        this.annotatedClass = annotatedClass;
        parse();
    }

    /**
     * Create AnnotatedClass instance.
     * @param key - class to create AnnotatedClass instance
     * @throws IllegalArgumentException - if class has no @Id field
     * @return - instance of AnnotatedClass
     */
    public static AnnotatedClass of(Class<?> key) {
        if (cacheClasses == null)
            cacheClasses = new HashMap<>();

        if (cacheClasses.containsKey(key)) {
            return cacheClasses.get(key);
        } else {
            AnnotatedClass cl = new AnnotatedClass(key);
            cacheClasses.put(key, cl);
            return cl;
        }
    }

    private void parse() {
        for (Field f : annotatedClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                idField = f;
            } else {
                fields.add(f);
            }
        }
        if (idField == null)
            throw new IllegalArgumentException();
    }

    public Field getId() {
        return idField;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public boolean is(Object entity) {
        return annotatedClass.equals(entity.getClass());
    }

    public String getSimpleName() {
        return annotatedClass.getSimpleName();
    }
}
