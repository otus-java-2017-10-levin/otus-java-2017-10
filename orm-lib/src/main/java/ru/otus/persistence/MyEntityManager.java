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
            connection = manager.getConnection("create db");
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

    /**
     * Find by primary key, using the specified properties.
     * Search for an entity of the specified class and primary key.
     * If the entity instance is contained in the persistence
     * context, it is returned from there.
     * If a vendor-specific property or hint is not recognized,
     * it is silently ignored.
     *
     * @param entityClass entity class
     * @param primaryKey  primary key
     * @param properties  standard and vendor-specific properties
     *                    and hints
     * @return the found entity instance or null if the entity does
     * not exist
     * @throws IllegalArgumentException if the first argument does
     *                                  not denote an entity type or the second argument is
     *                                  is not a valid type for that entitys primary key or
     *                                  is null
     * @since Java Persistence 2.0
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    /**
     * Find by primary key and lock.
     * Search for an entity of the specified class and primary key
     * and lock it with respect to the specified lock type.
     * If the entity instance is contained in the persistence context,
     * it is returned from there, and the effect of this method is
     * the same as if the lock method had been called on the entity.
     * <p> If the entity is found within the persistence context and the
     * lock mode type is pessimistic and the entity has a version
     * attribute, the persistence provider must perform optimistic
     * version checks when obtaining the database lock.  If these
     * checks fail, the <code>OptimisticLockException</code> will be thrown.
     * <p>If the lock mode type is pessimistic and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li> the <code>PessimisticLockException</code> will be thrown if the database
     * locking failure causes transaction-level rollback
     * <li> the <code>LockTimeoutException</code> will be thrown if the database
     * locking failure causes only statement-level rollback
     * </ul>
     *
     * @param entityClass entity class
     * @param primaryKey  primary key
     * @param lockMode    lock mode
     * @return the found entity instance or null if the entity does
     * not exist
     * @throws IllegalArgumentException     if the first argument does
     *                                      not denote an entity type or the second argument is
     *                                      not a valid type for that entity's primary key or
     *                                      is null
     * @throws TransactionRequiredException if there is no
     *                                      transaction and a lock mode other than NONE is
     *                                      specified
     * @throws OptimisticLockException      if the optimistic version
     *                                      check fails
     * @throws PessimisticLockException     if pessimistic locking
     *                                      fails and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking fails and
     *                                      only the statement is rolled back
     * @throws PersistenceException         if an unsupported lock call
     *                                      is made
     * @since Java Persistence 2.0
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }

    /**
     * Find by primary key and lock, using the specified properties.
     * Search for an entity of the specified class and primary key
     * and lock it with respect to the specified lock type.
     * If the entity instance is contained in the persistence context,
     * it is returned from there.
     * <p> If the entity is found
     * within the persistence context and the lock mode type
     * is pessimistic and the entity has a version attribute, the
     * persistence provider must perform optimistic version checks
     * when obtaining the database lock.  If these checks fail,
     * the <code>OptimisticLockException</code> will be thrown.
     * <p>If the lock mode type is pessimistic and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li> the <code>PessimisticLockException</code> will be thrown if the database
     * locking failure causes transaction-level rollback
     * <li> the <code>LockTimeoutException</code> will be thrown if the database
     * locking failure causes only statement-level rollback
     * </ul>
     * <p>If a vendor-specific property or hint is not recognized,
     * it is silently ignored.
     * <p>Portable applications should not rely on the standard timeout
     * hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not
     * be observed.
     *
     * @param entityClass entity class
     * @param primaryKey  primary key
     * @param lockMode    lock mode
     * @param properties  standard and vendor-specific properties
     *                    and hints
     * @return the found entity instance or null if the entity does
     * not exist
     * @throws IllegalArgumentException     if the first argument does
     *                                      not denote an entity type or the second argument is
     *                                      not a valid type for that entity's primary key or
     *                                      is null
     * @throws TransactionRequiredException if there is no
     *                                      transaction and a lock mode other than <code>NONE</code> is
     *                                      specified
     * @throws OptimisticLockException      if the optimistic version
     *                                      check fails
     * @throws PessimisticLockException     if pessimistic locking
     *                                      fails and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking fails and
     *                                      only the statement is rolled back
     * @throws PersistenceException         if an unsupported lock call
     *                                      is made
     * @since Java Persistence 2.0
     */
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
            throw new IllegalStateException("Please setup a transactin");

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

    /**
     * Lock an entity instance that is contained in the persistence
     * context with the specified lock mode type and with specified
     * properties.
     * <p>If a pessimistic lock mode type is specified and the entity
     * contains a version attribute, the persistence provider must
     * also perform optimistic version checks when obtaining the
     * database lock.  If these checks fail, the
     * <code>OptimisticLockException</code> will be thrown.
     * <p>If the lock mode type is pessimistic and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li> the <code>PessimisticLockException</code> will be thrown if the database
     * locking failure causes transaction-level rollback
     * <li> the <code>LockTimeoutException</code> will be thrown if the database
     * locking failure causes only statement-level rollback
     * </ul>
     * <p>If a vendor-specific property or hint is not recognized,
     * it is silently ignored.
     * <p>Portable applications should not rely on the standard timeout
     * hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not
     * be observed.
     *
     * @param entity     entity instance
     * @param lockMode   lock mode
     * @param properties standard and vendor-specific properties
     *                   and hints
     * @throws IllegalArgumentException     if the instance is not an
     *                                      entity or is a detached entity
     * @throws TransactionRequiredException if there is no
     *                                      transaction
     * @throws EntityNotFoundException      if the entity does not exist
     *                                      in the database when pessimistic locking is
     *                                      performed
     * @throws OptimisticLockException      if the optimistic version
     *                                      check fails
     * @throws PessimisticLockException     if pessimistic locking fails
     *                                      and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking fails and
     *                                      only the statement is rolled back
     * @throws PersistenceException         if an unsupported lock call
     *                                      is made
     * @since Java Persistence 2.0
     */
    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(Object entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh the state of the instance from the database, using
     * the specified properties, and overwriting changes made to
     * the entity, if any.
     * <p> If a vendor-specific property or hint is not recognized,
     * it is silently ignored.
     *
     * @param entity     entity instance
     * @param properties standard and vendor-specific properties
     *                   and hints
     * @throws IllegalArgumentException     if the instance is not
     *                                      an entity or the entity is not managed
     * @throws TransactionRequiredException if invoked on a
     *                                      container-managed entity manager of type
     *                                      <code>PersistenceContextType.TRANSACTION</code> and there is
     *                                      no transaction
     * @throws EntityNotFoundException      if the entity no longer
     *                                      exists in the database
     * @since Java Persistence 2.0
     */
    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh the state of the instance from the database,
     * overwriting changes made to the entity, if any, and
     * lock it with respect to given lock mode type.
     * <p>If the lock mode type is pessimistic and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li> the <code>PessimisticLockException</code> will be thrown if the database
     * locking failure causes transaction-level rollback
     * <li> the <code>LockTimeoutException</code> will be thrown if the
     * database locking failure causes only statement-level
     * rollback.
     * </ul>
     *
     * @param entity   entity instance
     * @param lockMode lock mode
     * @throws IllegalArgumentException     if the instance is not
     *                                      an entity or the entity is not managed
     * @throws TransactionRequiredException if there is no
     *                                      transaction and if invoked on a container-managed
     *                                      <code>EntityManager</code> instance with
     *                                      <code>PersistenceContextType.TRANSACTION</code> or with a lock mode
     *                                      other than <code>NONE</code>
     * @throws EntityNotFoundException      if the entity no longer exists
     *                                      in the database
     * @throws PessimisticLockException     if pessimistic locking fails
     *                                      and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking fails and
     *                                      only the statement is rolled back
     * @throws PersistenceException         if an unsupported lock call
     *                                      is made
     * @since Java Persistence 2.0
     */
    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh the state of the instance from the database,
     * overwriting changes made to the entity, if any, and
     * lock it with respect to given lock mode type and with
     * specified properties.
     * <p>If the lock mode type is pessimistic and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li> the <code>PessimisticLockException</code> will be thrown if the database
     * locking failure causes transaction-level rollback
     * <li> the <code>LockTimeoutException</code> will be thrown if the database
     * locking failure causes only statement-level rollback
     * </ul>
     * <p>If a vendor-specific property or hint is not recognized,
     * it is silently ignored.
     * <p>Portable applications should not rely on the standard timeout
     * hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not
     * be observed.
     *
     * @param entity     entity instance
     * @param lockMode   lock mode
     * @param properties standard and vendor-specific properties
     *                   and hints
     * @throws IllegalArgumentException     if the instance is not
     *                                      an entity or the entity is not managed
     * @throws TransactionRequiredException if there is no
     *                                      transaction and if invoked on a container-managed
     *                                      <code>EntityManager</code> instance with
     *                                      <code>PersistenceContextType.TRANSACTION</code> or with a lock mode
     *                                      other than <code>NONE</code>
     * @throws EntityNotFoundException      if the entity no longer exists
     *                                      in the database
     * @throws PessimisticLockException     if pessimistic locking fails
     *                                      and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking fails and
     *                                      only the statement is rolled back
     * @throws PersistenceException         if an unsupported lock call
     *                                      is made
     * @since Java Persistence 2.0
     */
    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        objects.clear();
    }

    /**
     * Remove the given entity from the persistence context, causing
     * a managed entity to become detached.  Unflushed changes made
     * to the entity if any (including removal of the entity),
     * will not be synchronized to the database.  Entities which
     * previously referenced the detached entity will continue to
     * reference it.
     *
     * @param entity entity instance
     * @throws IllegalArgumentException if the instance is not an
     *                                  entity
     * @since Java Persistence 2.0
     */
    @Override
    public void detach(Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the current lock mode for the entity instance.
     *
     * @param entity entity instance
     * @return lock mode
     * @throws TransactionRequiredException if there is no
     *                                      transaction
     * @throws IllegalArgumentException     if the instance is not a
     *                                      managed entity and a transaction is active
     * @since Java Persistence 2.0
     */
    @Override
    public LockModeType getLockMode(Object entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set an entity manager property or hint.
     * If a vendor-specific property or hint is not recognized, it is
     * silently ignored.
     *
     * @param propertyName name of property or hint
     * @param value        value for property or hint
     * @throws IllegalArgumentException if the second argument is
     *                                  not valid for the implementation
     * @since Java Persistence 2.0
     */
    @Override
    public void setProperty(String propertyName, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the properties and hints and associated values that are in effect
     * for the entity manager. Changing the contents of the map does
     * not change the configuration in effect.
     *
     * @return map of properties and hints in effect for entity manager
     * @since Java Persistence 2.0
     */
    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createQuery(String qlString) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of <code>TypedQuery</code> for executing a
     * criteria query.
     *
     * @param criteriaQuery a criteria query object
     * @return the new query instance
     * @throws IllegalArgumentException if the criteria query is
     *                                  found to be invalid
     * @since Java Persistence 2.0
     */
    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of Query for executing a criteria
     * update query.
     *
     * @param updateQuery a criteria update query object
     * @return the new query instance
     * @throws IllegalArgumentException if the update query is found to be invalid
     */
    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of Query for executing a criteria
     * delete query.
     *
     * @param deleteQuery a criteria delete query object
     * @return the new query instance
     * @throws IllegalArgumentException if the delete query isfound to be invalid
     */
    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of <code>TypedQuery</code> for executing a
     * Java Persistence query language statement.
     * The select list of the query must contain only a single
     * item, which must be assignable to the type specified by
     * the <code>resultClass</code> argument.
     *
     * @param qlString    a Java Persistence query string
     * @param resultClass the type of the query result
     * @return the new query instance
     * @throws IllegalArgumentException if the query string is found
     *                                  to be invalid or if the query result is found to
     *                                  not be assignable to the specified type
     * @since Java Persistence 2.0
     */
    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Query createNamedQuery(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of <code>TypedQuery</code> for executing a
     * Java Persistence query language named query.
     * The select list of the query must contain only a single
     * item, which must be assignable to the type specified by
     * the <code>resultClass</code> argument.
     *
     * @param name        the name of a query defined in metadata
     * @param resultClass the type of the query result
     * @return the new query instance
     * @throws IllegalArgumentException if a query has not been
     *                                  defined with the given name or if the query string is
     *                                  found to be invalid or if the query result is found to
     *                                  not be assignable to the specified type
     * @since Java Persistence 2.0
     */
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

    /**
     * Create an instance of StoredProcedureQuery for executing a
     * stored procedure in the database.
     *
     * @param name name assigned to the stored procedure query
     *             in metadata
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a query has not been
     *                                  defined with the given name
     */
    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of StoredProcedureQuery for executing a
     * stored procedure in the database.
     * Parameters must be registered before the stored procedure can
     * be executed.
     * If the stored procedure returns one or more result sets,
     * any result set will be returned as a list of type Object[].
     *
     * @param procedureName name of the stored procedure in the
     *                      database
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a stored procedure of the
     *                                  given name does not exist (or the query execution
     *                                  will fail)
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of StoredProcedureQuery for executing a
     * stored procedure in the database.
     * Parameters must be registered before the stored procedure can
     * be executed.
     * The resultClass arguments must be specified in the order in
     * which the result sets will be returned by the stored procedure
     * invocation.
     *
     * @param procedureName name of the stored procedure in the
     *                      database
     * @param resultClasses classes to which the result sets
     *                      produced by the stored procedure are to
     *                      be mapped
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a stored procedure of the
     *                                  given name does not exist (or the query execution
     *                                  will fail)
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an instance of StoredProcedureQuery for executing a
     * stored procedure in the database.
     * Parameters must be registered before the stored procedure can
     * be executed.
     * The resultSetMapping arguments must be specified in the order
     * in which the result sets will be returned by the stored
     * procedure invocation.
     *
     * @param procedureName     name of the stored procedure in the
     *                          database
     * @param resultSetMappings the names of the result set mappings
     *                          to be used in mapping result sets
     *                          returned by the stored procedure
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a stored procedure or
     *                                  result set mapping of the given name does not exist
     *                                  (or the query execution will fail)
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinTransaction() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determine whether the entity manager is joined to the
     * current transaction. Returns false if the entity manager
     * is not joined to the current transaction or if no
     * transaction is active
     *
     * @return boolean
     */
    @Override
    public boolean isJoinedToTransaction() {
        return false;
    }

    /**
     * Return an object of the specified type to allow access to the
     * provider-specific API.   If the provider's <code>EntityManager</code>
     * implementation does not support the specified class, the
     * <code>PersistenceException</code> is thrown.
     *
     * @param cls the class of the object to be returned.  This is
     *            normally either the underlying <code>EntityManager</code> implementation
     *            class or an interface that it implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not
     *                              support the call
     * @since Java Persistence 2.0
     */
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

        for (AnnotatedField f: ac.getFields(OneToOne.class)) {
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

    /**
     * Return the entity manager factory for the entity manager.
     *
     * @return EntityManagerFactory instance
     * @throws IllegalStateException if the entity manager has
     *                               been closed
     * @since Java Persistence 2.0
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return an instance of <code>CriteriaBuilder</code> for the creation of
     * <code>CriteriaQuery</code> objects.
     *
     * @return CriteriaBuilder instance
     * @throws IllegalStateException if the entity manager has
     *                               been closed
     * @since Java Persistence 2.0
     */
    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return an instance of <code>Metamodel</code> interface for access to the
     * metamodel of the persistence unit.
     *
     * @return Metamodel instance
     * @throws IllegalStateException if the entity manager has
     *                               been closed
     * @since Java Persistence 2.0
     */
    @Override
    public Metamodel getMetamodel() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a mutable EntityGraph that can be used to dynamically create an EntityGraph.
     *
     * @param rootType class of entity graph
     * @return entity graph
     * @since JPA 2.1
     */
    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a mutable copy of the named EntityGraph.  If there is no entity graph with the specified name, null
     * is returned.
     *
     * @param graphName name of an entity graph
     * @return entity graph
     * @since JPA 2.1
     */
    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a named EntityGraph. The returned EntityGraph should be considered immutable.
     *
     * @param graphName name of an existing entity graph
     * @return named entity graph
     * @throws IllegalArgumentException if there is no EntityGraph of the given name
     * @since JPA 2.1
     */
    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return all named EntityGraphs that have been defined for the provided class type.
     *
     * @param entityClass entity class
     * @return list of all entity graphs defined for the entity
     * @throws IllegalArgumentException if the class is not an entity
     * @since JPA 2.1
     */
    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        throw new UnsupportedOperationException();
    }
}