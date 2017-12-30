package ru.otus.persistence.annotations;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotatedClass {

    private static Map<Class<?>, AnnotatedClass> cacheClasses;
    private final Class<?> annotatedClass;
    private final NameGenerator generator;

    public Class<?> getAnnotatedClass() {
        return annotatedClass;
    }

    private final Map<String, AnnotatedField> fields = new LinkedHashMap<>();
    private AnnotatedField idField;

    private AnnotatedClass(Class<?> annotatedClass, NameGenerator nameGenerator) {
        this.annotatedClass = annotatedClass;
        generator = nameGenerator;
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
            AnnotatedClass cl = new AnnotatedClass(key, String::toUpperCase);
            cacheClasses.put(key, cl);
            return cl;
        }
    }

    public AnnotatedField getId() {
        return idField;
    }

    public List<AnnotatedField> getFields() {
        return new ArrayList<>(fields.values());
    }

    public boolean is(Object entity) {
        return annotatedClass.equals(entity.getClass());
    }

    public String getSimpleName() {
        return annotatedClass.getSimpleName();
    }

    public AnnotatedField getField(String dbName) {
        String name = generator.generate(dbName);
        return fields.get(name);
    }

    private void parse() {
        boolean hasId = false;
        annotatedClass.getFields();
        for (Field f : FieldUtils.getAllFields(annotatedClass)) {
            AnnotatedField af = new AnnotatedField(f);
            fields.put(generator.generate(f.getName()), af);
            if (af.isPrimaryKey()) {
                if (hasId)
                    throw new IllegalStateException("Class has more that one @Id");

                hasId = true;
                idField = af;
            }
        }

        if (!hasId)
            throw new IllegalArgumentException();
    }
}
