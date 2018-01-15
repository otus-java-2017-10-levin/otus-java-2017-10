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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
            connection.execQuery(QueryFactory.getInsertQuery(annotationManager, annotatedClass.getAnnotatedClass(), structure.getId() != 0), statement -> {
                int pos = 1;
                for (AnnotatedField f : annotatedClass.getFields()) {
                    try {
                        Object value = Objects.requireNonNull(f.getFieldValue(entity));
                        if (f.contains(ManyToOne.class)) {
                            long id = (long) annotationManager.getId(f.getType()).getFieldValue(value);
                            statement.setLong(pos++, id);
                        } else if (!f.contains(OneToOne.class) &&
                                !f.contains(OneToMany.class) &&
                                !f.contains(annotationManager.getIdAnnotation())) {
                            statement.setString(pos++, value.toString());
                        } else if (f.contains((annotationManager.getIdAnnotation()))) {
                            if ((Long)value != 0) {
                                statement.setLong(pos++, (Long)value);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
//                statement.addBatch();
                statement.execute();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                long id = -1;
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                }
                try {
                    idField.setFieldValue(entity, id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        long id = (long) idField.getFieldValue(entity);
        structure.setId(id);
        System.out.println(structure.getEntity());
        return id;
    }

    @Override
    public <T> T visit(@NotNull Class<T> entityClass, long primaryKey) {
        List<Class<?>> foreignClasses = annotationManager.getAnnotatedClass(entityClass).getFields(OneToOne.class).stream().map(AnnotatedField::getType).collect(Collectors.toList());
        foreignClasses.add(entityClass);

        Map<CacheUnit, Object> cache = new HashMap<>();
        Map<String, Object> map = null;

        try {
            try (DBConnection connection = dbManager.getConnection()) {

                String selectQuery = QueryFactory.getSelectQuery(annotationManager, entityClass, annotationManager.getId(entityClass), primaryKey);
                map = connection.execQuery(selectQuery, result -> {
                    result.next();
                    return readResultLine(result);
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

    private Map<String, Object> readResultLine(ResultSet result) throws SQLException {
        ResultSetMetaData rsmd = result.getMetaData();
        Map<String, Object> m = new HashMap<>();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String name = rsmd.getColumnLabel(i);
            m.put(name, result.getObject(i));
        }
        return m;
    }

    private <T> List<T> loadObjects(@NotNull final Class<T> entityClass,
                                    @NotNull final AnnotatedField field,
                                    final long value) {
        List<T> result = new ArrayList<>();

        try (DBConnection connection = dbManager.getConnection()) {
            result = connection.execQuery(QueryFactory.getSelectQuery(annotationManager, entityClass, field, value), resultSet -> {
                ArrayList<T> ts = new ArrayList<>();
                // плохо ли создавать объекты пока не прочитаны все результаты из бд?
                while (resultSet.next()) {
                    ts.add(buildObject(entityClass, readResultLine(resultSet)));
                }
                return ts;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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

            for (AnnotatedField f : ac.getFields(OneToMany.class)) {
                try {
                    Collection<?> col = (Collection)f.getFieldValue(object);
                    String mappedBy = Objects.requireNonNull(f.getAnnotation(OneToMany.class)).mappedBy();
                    if (mappedBy.equals(""))
                        throw new IllegalStateException();

                    if (col == null)
                        continue;

                    col.forEach(elem -> {
                        if (elem == null)
                            throw new IllegalArgumentException();
                        AnnotatedField field = annotationManager.getAnnotatedClass(elem.getClass()).getField(mappedBy);
                        try {
                            field.setFieldValue(elem, object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void cacheObjects(@NotNull List<Class<?>> foreignClass,
                             @NotNull Map<String, Object> map,
                             @NotNull Map<CacheUnit, Object> cache) {
        foreignClass.forEach(aClass -> cacheObject(aClass, map, cache));
    }

    private void cacheObject(@NotNull Class<?> foreignClass,
                              @NotNull Map<String, Object> map,
                              @NotNull Map<CacheUnit, Object> cache) {

            Object object = buildObject(foreignClass, map);
            Class<?> cl = object.getClass();

            CacheUnit id = createCacheUnit(cl, object);
            cache.put(id, object);
    }

    private CacheUnit createCacheUnit(@NotNull final Class<?> cl,
                                      @NotNull final Object object) {
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

    private <T> T buildObject(@NotNull final Class<T> entityClass,
                              @NotNull final Map<String, Object> params) {
        ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
        List<AnnotatedField> fields = annotationManager.getAnnotatedClass(entityClass).getFields();
        for (AnnotatedField f : fields) {
            if (f.contains(OneToMany.class)) {
                String mappedBy = Objects.requireNonNull(f.getAnnotation(OneToMany.class)).mappedBy();
                if (mappedBy.equals(""))
                    throw new IllegalStateException();

                Class<?> componentType = f.getComponentType();
                if (componentType == null)
                    throw new IllegalStateException();

                AnnotatedClass component = annotationManager.getAnnotatedClass(componentType);
                AnnotatedField field = component.getField(mappedBy);
                String fullName = annotationManager.getId(entityClass).getFullName("_").toUpperCase();
                Long value = (Long) params.get(fullName);
                Collection<?> col = loadObjects(componentType, field, value);
                builder.set(f, col);
            } else if (!f.contains(OneToOne.class) && !f.contains(ManyToOne.class)) {
                String fullName = f.getFullName("_").toUpperCase();
                if (!params.containsKey(fullName))
                    throw new IllegalArgumentException();
                builder.set(f, params.get(fullName));
            }
        }
        return builder.build();
    }
}