package ru.otus.persistence;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.jdbc.DbManagerFactory;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

class MyEntityManager implements EntityManager {

    private final DbManager manager;
    private final DBConnection connection;
    private final PersistenceParams params;
    private final List<AnnotatedClass> annotatedClass = new ArrayList<>();
    private boolean isOpen;
    private final Map<AnnotatedClass, Set<Object>> objects = new HashMap<>();

    MyEntityManager(Map params) {
        this.params = new PersistenceParams(params);
        manager = DbManagerFactory.createDataBaseManager(this.params.getConnectionData());
        connection = manager.getConnection();
        try {
            loadEntityClasses();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        isOpen = true;
        dropTables();
        createTables();
    }

    private void loadEntityClasses() throws ClassNotFoundException {
        if (params == null || params.getEntityClasses().size() == 0)
            throw new IllegalStateException("There is no entity classes in persistence.xml");

        for (String className : params.getEntityClasses()) {
            if (className == null)
                throw new IllegalArgumentException("className is null");

            AnnotatedClass cl = AnnotatedClass.of(Class.forName(className));

            annotatedClass.add(cl);
        }
    }

    @Override
    public void persist(Object entity) {
        if (!isOpen)
            throw new IllegalStateException("EntityManager is closed");

        AnnotatedClass cl = checkEntity(entity);

        Set<Object> set = objects.containsKey(cl) ? objects.get(cl) : new HashSet<>();

        if (set.contains(entity))
            throw new EntityExistsException();

        set.add(entity);
        objects.put(cl, set);
    }

    private AnnotatedClass checkEntity(Object entity) {
        for (AnnotatedClass cl : annotatedClass) {
            if (cl.is(entity)) {
                return cl;
            }
        }
        throw new IllegalArgumentException("is not an entity");
    }

    @Override
    public <T> T merge(T entity) {
        return null;
    }

    @Override
    public void remove(Object entity) {

    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        if (!isOpen)
            throw new IllegalStateException();

        final AnnotatedClass cl = AnnotatedClass.of(entityClass);

        Class<?> cl1 = cl.getId().getType();
        if (cl1.isPrimitive())
            cl1 = ClassUtils.primitiveToWrapper(cl1);
        Class<?> cl2 = primaryKey.getClass();
        if (!annotatedClass.contains(cl) ||
                !cl1.equals(cl2)) {
            throw new IllegalArgumentException();
        }

        long id = -1;
        try {
            id = ((Long) primaryKey);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        T cls = null;
        try {
            cls = connection.execQuery(QueryFactory.getSelectQuery(cl, id), result -> {

                ObjectBuilder<T> builder = new ObjectBuilder<>(entityClass);
                ResultSetMetaData rsmd = result.getMetaData();
                result.next();

                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String name = rsmd.getColumnName(i);
                    builder.set(cl.getField(name).getName(), result.getObject(i));
                }
                return builder.build();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cls;
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return null;
    }


    @Override
    public void flush() {
        if (!isOpen)
            throw new IllegalStateException();

        saveObjects();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {

    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void refresh(Object entity) {

    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public boolean contains(Object entity) {
        return false;
    }


    @Override
    public Query createQuery(String qlString) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createNamedQuery(String name) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createNativeQuery(String sqlString) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return null;
    }

    @Override
    public void joinTransaction() {

    }

    @Override
    public Object getDelegate() {
        return null;
    }

    @Override
    public void close() {
        if (!isOpen)
            throw new IllegalStateException();

        saveObjects();
        manager.close();
        isOpen = false;
    }

    private void saveObjects() {
        for (Map.Entry<AnnotatedClass, Set<Object>> entry : objects.entrySet()) {
            saveObject(entry.getKey(), entry.getValue());
        }
    }

    private void saveObject(AnnotatedClass annotatedClass, Set<Object> objects) {
        String query = QueryFactory.getInsertQuery(annotatedClass);
        connection.execQuery(query, statement -> {
            for (Object object : objects) {
                int count = 1;
                for (AnnotatedField field : annotatedClass.getFields()) {
                    try {
                        if (!field.contains(Id.class))
                            statement.setString(count++, FieldUtils.readField(field.getField(), object, true).toString());

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                statement.execute();
                long id = connection.getLastInsertedId();
                saveObjectId(annotatedClass, object, id);
            }
        });
    }

    private void saveObjectId(AnnotatedClass annotatedClass, Object object, long id) {

        Class<?> cl = annotatedClass.getAnnotatedClass();
        try {
            Method m = cl.getMethod("setId", long.class);
            try {
                m.invoke(object, id);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void dropTables() {
        for (AnnotatedClass cl : annotatedClass) {
            String query = QueryFactory.getDropTableQuery(cl);
            connection.execQuery(query);
        }
    }

    private void createTables() {
        for (AnnotatedClass cl : annotatedClass) {
            String query = QueryFactory.createTableQuery(cl);
            connection.execQuery(query);
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public EntityTransaction getTransaction() {
        throw new UnsupportedOperationException();
    }
}