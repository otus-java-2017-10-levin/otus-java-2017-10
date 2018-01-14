package ru.otus.persistence;

import org.apache.commons.lang3.ClassUtils;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.jdbc.DbManagerFactory;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.*;

@SuppressWarnings("ALL")
class MyEntityManager implements EntityManager {

    private final DbManager manager;
    private AnnotationManager annotationManager;
    private final static Class<? extends Annotation> idClass = Id.class;
    private boolean isOpen;
    private final Map<Class<?>, Set<Object>> objects = new HashMap<>();

    private FlushModeType flushModeType = FlushModeType.COMMIT;
    private Persister persister;

    MyEntityManager(Map persistenceParams) {
        PersistenceParams persistenceParams1 = new PersistenceParams(persistenceParams);
        manager = DbManagerFactory.createDataBaseManager(persistenceParams1.getConnectionData());

        if (persistenceParams == null || persistenceParams1.getEntityClasses().size() == 0)
            throw new IllegalStateException("There is no entity classes in persistence.xml");

        try {
            annotationManager = new AnnotationManager(idClass, persistenceParams1.getEntityClasses().toArray(new String[0]));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        isOpen = true;
        try (DBConnection connection = manager.getConnection()) {
            dropTables(connection);
            createTables(connection);
            addConstraints(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createPersister();
    }

    private void createPersister() {
        persister = new PersisterImpl.Builder().setAnnotationManager(annotationManager).setDbManager(manager).build();
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
        throw new UnsupportedOperationException();
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

        return persister.find(entityClass, id);
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
        flushModeType = flushMode;
    }

    @Override
    public FlushModeType getFlushMode() {
        return flushModeType;
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void refresh(Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public boolean contains(Object entity) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinTransaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDelegate() {
        throw new UnsupportedOperationException();
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
            entry.getValue().forEach(object -> {
                if (persister.save(object) == -1)
                    throw new IllegalStateException("id is -1");
            });
        }

        for (Map.Entry<Class<?>, Set<Object>> entry : objects.entrySet()) {
            entry.getValue().forEach(object -> {
                try {
                    persister.updateKeys(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }

        objects.clear();
    }

    private void dropTables(DBConnection connection) {
        for (Class<?> cl : annotationManager.getAllClasses()) {
            String query = QueryFactory.getDropTableQuery(annotationManager, cl);
            connection.execQuery(query);
        }
    }

    private void createTables(DBConnection connection) {
        for (Class<?> cl : annotationManager.getAllClasses()) {
            String query = QueryFactory.createTableQuery(annotationManager, cl);
            connection.execQuery(query);
        }
    }

    private void addConstraints(DBConnection connection) {
        annotationManager.getAllClasses().forEach(entityClass -> addConstraint(connection, entityClass));
    }

    private void addConstraint(DBConnection connection, Class<?> entityClass) {
        AnnotatedClass ac = annotationManager.getAnnotatedClass(entityClass);

        for (AnnotatedField f: ac.getFields(OneToOne.class)) {
            if (!annotationManager.contains(f.getType()))
                throw new IllegalArgumentException("class is not an entity: " + f.getType());

            AnnotatedClass foreignClass = annotationManager.getAnnotatedClass(f.getType());
            connection.execQuery(QueryFactory.getFKey(new ConstraintImpl(ac, foreignClass, f.getName())));
        }
    }
    // alter table UserDataSet add constraint foreign key (address_id) references AddressDataSet
    // ALTER TABLE USERDATASET ADD CONSTRAINT FOREIGN KEY (PHONE) REFERENCES PHONESDATASET


    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public EntityTransaction getTransaction() {
        throw new UnsupportedOperationException();
    }
}