package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.persistence.caching.CacheUnit;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.stream.Collectors;

public class SQLEntityVisitor implements EntityVisitor {
    private final AnnotationManager annotationManager;
    private final DbManager dbManager;


    SQLEntityVisitor(AnnotationManager annotationManager, DbManager dbManager) {
        this.annotationManager = annotationManager;
        this.dbManager = dbManager;
    }

    @Override
    public long visit(EntityStructure structure) throws IllegalAccessException {
        AnnotatedClass annotatedClass = structure.getEntityClass();
        Object entity = structure.getEntity();
        AnnotatedField idField = annotationManager.getId(annotatedClass.getAnnotatedClass());
        try (DBConnection connection = dbManager.getConnection()) {
            connection.execQuery(QueryFactory.getInsertQuery(annotationManager, annotatedClass.getAnnotatedClass()), statement -> {
                int pos = 1;
                for (AnnotatedField f : annotatedClass.getFields()) {
                    try {
                        Object value = Objects.requireNonNull(f.getFieldValue(entity));
                        if (!f.contains(OneToOne.class) &&
                                !f.contains(OneToMany.class) &&
                                !f.contains(ManyToOne.class) &&
                                !f.contains(annotationManager.getIdAnnotation())) {
                            statement.setString(pos++, value.toString());
                        } else if (f.contains((annotationManager.getIdAnnotation()))) {
                            if (structure.getId() != 0) {
                                statement.setLong(pos++, structure.getId());
                            } else {
                                statement.setString(pos++, null);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                statement.addBatch();
                int id[] = statement.executeBatch();
                try {
                    idField.setFieldValue(entity, id[0]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        long id = (long) idField.getFieldValue(entity);
        structure.setId(id);
        return id;
    }

    @Override
    public <T> T load(@NotNull Class<T> entityClass, long primaryKey) {
        List<Class<?>> foreignClasses = annotationManager.getAnnotatedClass(entityClass).getFields(OneToOne.class).stream().map(AnnotatedField::getType).collect(Collectors.toList());
        foreignClasses.add(entityClass);

        Map<CacheUnit, Object> cache = new HashMap<>();
        Map<String, Object> map = null;

        try {
            try (DBConnection connection = dbManager.getConnection()) {

                String selectQuery = QueryFactory.getSelectQuery(annotationManager, primaryKey, entityClass);
                map = connection.execQuery(selectQuery, result -> {
                    ResultSetMetaData rsmd = result.getMetaData();
                    Map<String, Object> m = new HashMap<>();

                    result.next();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String name = rsmd.getColumnLabel(i);
                        m.put(name, result.getObject(i));
                    }
                    return m;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map == null)
            throw new IllegalArgumentException("map is null");

        cacheObjects(foreignClasses, map, cache);
        setFKeys(cache, map);
        return entityClass.cast(cache.get(new CacheUnit(primaryKey, entityClass)));
    }

    private void setFKeys(Map<CacheUnit, Object> cache, Map<String, Object> map) {
        cache.forEach((cacheUnit, object) -> {
            Class<?> aClass = object.getClass();
            AnnotatedClass ac = annotationManager.getAnnotatedClass(aClass);
            for (AnnotatedField f : ac.getFields(OneToOne.class)) {

                Long id = (Long) map.get(f.getFullName("_").toUpperCase());
                CacheUnit c = new CacheUnit(id, f.getType());
                try {
                    f.setFieldValue(object, cache.get(c));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void cacheObjects(@NotNull List<Class<?>> foreignClasses,
                              @NotNull Map<String, Object> map,
                              @NotNull Map<CacheUnit, Object> cache) {
        foreignClasses.forEach(aClass -> {
            Object object = buildObject(aClass, map);
            Class<?> cl = object.getClass();

            CacheUnit id = createCacheUnit(cl, object);
            cache.put(id, object);

        });
    }

    private CacheUnit createCacheUnit(@NotNull Class<?> cl,
                                      @NotNull Object object) {
        Object value = null;
        try {
            value = annotationManager.getId(cl).getFieldValue(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (value == null)
            throw new IllegalStateException("id is null");

        return new CacheUnit((Long) value, object.getClass());
    }

    private <T> T buildObject(@NotNull Class<T> entityClass, Map<String, Object> params) {
        ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
        List<AnnotatedField> fields = annotationManager.getAnnotatedClass(entityClass).getFields();
        for (AnnotatedField f : fields) {
            if (!f.contains(OneToOne.class)) {
                String fullName = f.getFullName("_").toUpperCase();
                if (!params.containsKey(fullName))
                    throw new IllegalArgumentException();
                builder.set(f, params.get(fullName));
            }
        }
        return builder.build();
    }

    @Override
    public void visit(ForeignKeys keys) {
        try (DBConnection connection = dbManager.getConnection()) {
            connection.execQuery(QueryFactory.getUpdateKeysQuery(annotationManager, keys.getEntityClass()), statement -> {
                int pos = 1;
                for (Long l : keys.getKeys().values())
                    statement.setLong(pos++, l);
                statement.setLong(pos, keys.getId());
                statement.execute();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}