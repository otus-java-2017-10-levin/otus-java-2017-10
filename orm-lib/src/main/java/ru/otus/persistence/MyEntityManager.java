package ru.otus.persistence;

import org.apache.commons.lang3.ClassUtils;
import ru.otus.jdbc.DBConnection;
import ru.otus.jdbc.DbManager;
import ru.otus.jdbc.DbManagerFactory;
import ru.otus.persistence.annotations.AnnotatedClass;
import ru.otus.persistence.annotations.AnnotatedField;
import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.lang.annotation.Annotation;
import java.sql.SQLException;
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
    private EntityTransaction currentTransaction;

    MyEntityManager(Map persistenceParams) throws Exception {
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

        DBConnection connection = null;
        try {
            connection = manager.getConnection();
            dropTables(connection);
            createTables(connection);
            addConstraints(connection);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.close();
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

    private void checkEntity(Object entity) {
        if (!annotationManager.contains(entity.getClass()))
            throw new IllegalArgumentException("is not an entity");
    }

    @Override
    public <T> T merge(T entity) {
        throw new UnsupportedOperationException();
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
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }


    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        if (!isOpen)
            throw new IllegalStateException();

        if (currentTransaction == null || !currentTransaction.isActive())
            throw new IllegalStateException("Please setup a transaction");

        try {
            saveObjects();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
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
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        objects.clear();
        try {
            persister.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void detach(Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object entity) {
        throw new UnsupportedOperationException();
    }


    @Override
    public LockModeType getLockMode(Object entity) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setProperty(String propertyName, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createQuery(String qlString) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        throw new UnsupportedOperationException();
    }

     @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNamedQuery(String name) {
        throw new UnsupportedOperationException();
    }

      @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
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
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinTransaction() {
        throw new UnsupportedOperationException();
    }

     @Override
    public boolean isJoinedToTransaction() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
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

        manager.close();
        isOpen = false;
    }

    private void saveObjects() throws SQLException {
        try {
            for (Map.Entry<Class<?>, Set<Object>> entry : objects.entrySet()) {
                entry.getValue().forEach(object -> {
                    if (persister.save(object) == -1)
                        throw new IllegalStateException("id is -1");
                });
            }
            persister.commit();

            for (Map.Entry<Class<?>, Set<Object>> entry : objects.entrySet()) {
                entry.getValue().forEach(object -> {
                    try {
                        persister.updateKeys(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }
            persister.commit();

        } catch (Exception e) {
            try {
                persister.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
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

        for (AnnotatedField f : ac.getFields(OneToOne.class)) {
            if (!annotationManager.contains(f.getType()))
                throw new IllegalArgumentException("class is not an entity: " + f.getType());

            AnnotatedClass foreignClass = annotationManager.getAnnotatedClass(f.getType());
            connection.execQuery(QueryFactory.getFKey(new ConstraintImpl(ac, foreignClass, f.getName())));
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public EntityTransaction getTransaction() {
        if (currentTransaction == null)
            currentTransaction = new MyEntityTransaction(this);

        return currentTransaction;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Metamodel getMetamodel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        throw new UnsupportedOperationException();
    }
}