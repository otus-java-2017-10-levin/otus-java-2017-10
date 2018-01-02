package ru.otus.persistence;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.jdbc.DbManagerFactory;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

class MyEntityManager implements EntityManager {

    private final DbManager manager;
    private final DBConnection connection;
    private AnnotationManager annotationManager;
    private final static Class<? extends Annotation> idClass = Id.class;
    private boolean isOpen;
    private final Map<Class<?>, Set<Object>> objects = new HashMap<>();

    MyEntityManager(Map persistenceParams) {
        PersistenceParams persistenceParams1 = new PersistenceParams(persistenceParams);
        manager = DbManagerFactory.createDataBaseManager(persistenceParams1.getConnectionData());
        connection = manager.getConnection();

        if (persistenceParams == null || persistenceParams1.getEntityClasses().size() == 0)
            throw new IllegalStateException("There is no entity classes in persistence.xml");

        try {
            annotationManager = new AnnotationManager(idClass, persistenceParams1.getEntityClasses().toArray(new String[0]));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        isOpen = true;
        dropTables();
        createTables();
    }

    @Override
    public void persist(Object entity) {
        if (!isOpen)
            throw new IllegalStateException("EntityManager is closed");

        checkEntity(entity);
        Class<?> entityClass = entity.getClass();

        Set<Object> set = objects.containsKey(entityClass) ? objects.get(entityClass) : new HashSet<>();

        if (set.contains(entity))
            throw new EntityExistsException();

        set.add(entity);
        objects.put(entityClass, set);
    }

    // if this object is instance of annotated class
    private void checkEntity(Object entity) {
        if (!annotationManager.contains(entity.getClass()))
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

        if (!annotationManager.contains(entityClass))
            throw new IllegalArgumentException("wrong entity class");

        Class<?> idType = annotationManager.getId(entityClass).getType();

        idType = ClassUtils.primitiveToWrapper(idType);

        Class<?> cl2 = primaryKey.getClass();
        if (!idType.equals(cl2)) {
            throw new IllegalArgumentException();
        }

        long id = -1;
        try {
            id = ((Long) primaryKey);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return PersistenceHelper.find(connection, annotationManager, entityClass, id);
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
        for (Map.Entry<Class<?>, Set<Object>> entry : objects.entrySet()) {
            PersistenceHelper.saveObjects(connection, annotationManager, entry.getKey(), entry.getValue());
        }
    }



    private void dropTables() {
        for (Class<?> cl : annotationManager.getAllClasses()) {
            String query = QueryFactory.getDropTableQuery(annotationManager, cl);
            connection.execQuery(query);
        }
    }

    private void createTables() {
        for (Class<?> cl : annotationManager.getAllClasses()) {
            String query = QueryFactory.createTableQuery(annotationManager, cl);
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