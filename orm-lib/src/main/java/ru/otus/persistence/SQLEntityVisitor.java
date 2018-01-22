package ru.otus.persistence;

import org.jetbrains.annotations.NotNull;
import ru.otus.jdbc.DBConnection;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.persistence.caching.CacheUnit;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SQLEntityVisitor implements EntityVisitor {
    private static int counter = 0;
    private final int id = counter++;
    private final AnnotationManager annotationManager;
    private final MutableConfiguration<CacheUnit, Object> config = new MutableConfiguration<CacheUnit, Object>()
            .setTypes(CacheUnit.class, Object.class)
            .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 3)))
            .setStatisticsEnabled(true);

    private final Cache<CacheUnit, Object> cache = Caching.getCachingProvider().getCacheManager().createCache("L1-Cache-" + id, config);

    SQLEntityVisitor(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    @Override
    public long save(@NotNull EntityStructure structure, @NotNull DBConnection connection) throws IllegalAccessException {
        AnnotatedClass annotatedClass = structure.getEntityClass();
        Object entity = structure.getEntity();
        AnnotatedField idField = annotationManager.getId(annotatedClass.getAnnotatedClass());
        try {
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
                            if ((Long) value != 0) {
                                statement.setLong(pos++, (Long) value);
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
                generatedKeys.close();
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
        return id;
    }

    @Override
    public void save(@NotNull ForeignKeys keys, @NotNull DBConnection connection) {
        try {
            connection.execQuery(QueryFactory.getUpdateKeysQuery(annotationManager, keys.getEntityClass()), statement -> {
                int pos = 1;
                for (Long l : keys.getKeys().values())
                    statement.setLong(pos++, l);
                statement.setLong(pos, keys.getId());
                statement.executeUpdate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T load(@NotNull Class<T> entityClass, long primaryKey, @NotNull DBConnection connection) {

        final CacheUnit key = new CacheUnit(primaryKey, entityClass);
        final Object res = cache.get(key);
        if (res != null) {
            return entityClass.cast(res);
        }

//        List<Class<?>> foreignClasses = annotationManager.getAnnotatedClass(entityClass).getFields(Arrays.asList(OneToOne.class, ManyToOne.class)).stream().map(AnnotatedField::getType).collect(Collectors.toList());
//        foreignClasses.add(entityClass);

        Map<String, Object> map = null;

        try {
            String selectQuery = QueryFactory.getSelectQuery(annotationManager, entityClass, annotationManager.getId(entityClass), primaryKey);
            map = connection.execQuery(selectQuery, result -> {
                if (result.next()) {
                    return readResultLine(result);
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map == null)
            return null;

        cacheObject(entityClass, map, connection);
        setFKeys(entityClass, map, connection);
        return entityClass.cast(cache.get(key));
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
                                    final long value,
                                    DBConnection connection) {
        List<T> result = new ArrayList<>();

        try {
            result = connection.execQuery(QueryFactory.getSelectQuery(annotationManager, entityClass, field, value), resultSet -> {
                ArrayList<T> ts = new ArrayList<>();
                while (resultSet.next()) {
                    ts.add(buildObject(entityClass, readResultLine(resultSet), connection));
                }
                resultSet.close();
                return ts;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void setFKeys(Class<?> entityClass, Map<String, Object> map, DBConnection connection) {

        AnnotatedClass ac = annotationManager.getAnnotatedClass(entityClass);
        final String fullName = annotationManager.getId(entityClass).getFullName("_").toUpperCase();
        final Object object = cache.get(new CacheUnit((Long) map.get(fullName), entityClass));

        for (AnnotatedField f : ac.getFields(OneToOne.class)) {

            Long id = (Long) map.get(f.getFullName("_").toUpperCase());
            CacheUnit c = new CacheUnit(id, f.getType());
            Object value = cache.get(c);
            if (value == null) {
                value = load(f.getType(), id, connection);
            }
            try {
                f.setFieldValue(object, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (AnnotatedField f : ac.getFields(ManyToOne.class)) {
            String name = f.getFullName("_").toUpperCase();
            long id = (Long) map.get(name);
            CacheUnit c = new CacheUnit(id, f.getType());
            Object value = cache.get(c);
            if (value == null) {
                value = load(f.getType(), id, connection);
            }
            try {
                f.setFieldValue(object, cache.get(new CacheUnit(id, f.getType())));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (AnnotatedField f : ac.getFields(OneToMany.class)) {
            try {
                Collection<?> col = (Collection) f.getFieldValue(object);
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
    }

    private void cacheObjects(@NotNull List<Class<?>> foreignClass,
                              @NotNull Map<String, Object> map,
                              @NotNull DBConnection connection) {
        foreignClass.forEach(aClass -> cacheObject(aClass, map, connection));
    }

    private void cacheObject(@NotNull Class<?> foreignClass,
                             @NotNull Map<String, Object> map,
                             @NotNull DBConnection connection) {

        Object object = buildObject(foreignClass, map, connection);
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

    /*
     *
     * SELECT USERDATASET.NAME AS USERDATASET_NAME,
     *        USERDATASET.AGE AS USERDATASET_AGE,
     *        USERDATASET.ADDRESS AS USERDATASET_ADDRESS,
     *        USERDATASET.ID AS USERDATASET_ID,
     *        USERDATASET.NAME AS USERDATASET_NAME,
     *        USERDATASET.AGE AS USERDATASET_AGE,
     *        USERDATASET.ADDRESS AS USERDATASET_ADDRESS,
     *        USERDATASET.ID AS USERDATASET_ID
     *  FROM USERDATASET WHERE USERDATASET.ID = 1

     * */
    private <T> T buildObject(@NotNull final Class<T> entityClass,
                              @NotNull final Map<String, Object> params,
                              DBConnection connection) {
        final String idName = annotationManager.getId(entityClass).getFullName("_").toUpperCase();
        final CacheUnit key = new CacheUnit((Long) params.get(idName), entityClass);
        if (cache.containsKey(key)) {
            return entityClass.cast(cache.get(key));
        }
        ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
        List<AnnotatedField> fields = annotationManager.getAnnotatedClass(entityClass).getFields();
        for (AnnotatedField f : fields) {
            if (f.contains(OneToMany.class)) {
                String mappedBy = Objects.requireNonNull(f.getAnnotation(OneToMany.class)).mappedBy();
                if (mappedBy.equals(""))
                    throw new IllegalStateException();

                final Class<?> componentType = f.getComponentType();
                if (componentType == null)
                    throw new IllegalStateException();

                AnnotatedClass component = annotationManager.getAnnotatedClass(componentType);
                Long value = (Long) params.get(idName);
                Collection<?> col = loadObjects(componentType, component.getField(mappedBy), value, connection);
                builder.set(f, col);
            } else if (f.contains(ManyToOne.class)) {

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