package ru.otus.persistence;

import ru.otus.persistence.xml.PersistenceParams;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MyEntityManagerFactory implements EntityManagerFactory {

    private final List<EntityManager> managers = new ArrayList<>();
    private final String XML = "META-INF/persistence.xml";
    private boolean isOpen = true;
    private final String persistenceUnit;

    public MyEntityManagerFactory(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    @Override
    public EntityManager createEntityManager() {
        PersistenceParams params = new PersistenceParams(persistenceUnit, XML);
        return createEntityManager(params.getParameters());
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        EntityManager manager = null;
        try {
            manager = new MyEntityManager(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        managers.add(manager);
        return manager;
    }

    /**
     * Create a new JTA application-managed EntityManager with the specified synchronization type.  This method
     * returns a new EntityManager instance each time it is invoked.  The isOpen method will return true on the
     * returned instance.
     *
     * @param synchronizationType how and when the entity manager should be synchronized with the current JTA
     *                            transaction
     * @return entity manager instance
     * @throws IllegalStateException if the entity manager factory has been configured for resource-local entity
     *                               managers or has been closed
     */
    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return null;
    }

    /**
     * Create a new JTA application-managed EntityManager with the specified synchronization type and Map of properties.
     * This method returns a new EntityManager instance each time it is invoked.  The isOpen method will return true
     * on the returned instance.
     *
     * @param synchronizationType how and when the entity manager
     *                            should be synchronized with the current JTA transaction
     * @param map                 properties for entity manager; may be null
     * @return entity manager instance
     * @throws IllegalStateException if the entity manager factory has been configured for resource-local entity
     *                               managers or has been closed
     */
    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return null;
    }

    /**
     * Return an instance of <code>CriteriaBuilder</code> for the creation of
     * <code>CriteriaQuery</code> objects.
     *
     * @return CriteriaBuilder instance
     * @throws IllegalStateException if the entity manager factory
     *                               has been closed
     * @since Java Persistence 2.0
     */
    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    /**
     * Return an instance of <code>Metamodel</code> interface for access to the
     * metamodel of the persistence unit.
     *
     * @return Metamodel instance
     * @throws IllegalStateException if the entity manager factory
     *                               has been closed
     * @since Java Persistence 2.0
     */
    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public void close() {
        managers.forEach(EntityManager::close);
        isOpen = false;
    }

    /**
     * Get the properties and associated values that are in effect
     * for the entity manager factory. Changing the contents of the
     * map does not change the configuration in effect.
     *
     * @return properties
     * @throws IllegalStateException if the entity manager factory
     *                               has been closed
     * @since Java Persistence 2.0
     */
    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    /**
     * Access the cache that is associated with the entity manager
     * factory (the "second level cache").
     *
     * @return instance of the <code>Cache</code> interface
     * @throws IllegalStateException if the entity manager factory
     *                               has been closed
     * @since Java Persistence 2.0
     */
    @Override
    public Cache getCache() {
        return null;
    }

    /**
     * Return interface providing access to utility methods
     * for the persistence unit.
     *
     * @return <code>PersistenceUnitUtil</code> interface
     * @throws IllegalStateException if the entity manager factory
     *                               has been closed
     * @since Java Persistence 2.0
     */
    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return null;
    }

    /**
     * Define the query, typed query, or stored procedure query as
     * a named query such that future query objects can be created
     * from it using the createNamedQuery methods.
     * Any configuration of the query object (except for actual
     * parameter binding) in effect when the named query is added
     * is retained as part of the named query definition.
     * This includes configuration information such as max results,
     * hints, flush mode, lock mode, result set mapping information,
     * and information about stored procedure parameters.
     * When the query is executed, information that can be set
     * by means of the Query API can be overridden. Information
     * that is overridden does not affect the named query as
     * registered with the entity manager factory, and thus does
     * not affect subsequent query objects created from it by
     * means of the createNamedQuery method.
     * If a named query of the same name has been previously
     * defined, either statically via metadata or via this method,
     * that query definition is replaced.
     *
     * @param name  name for the query
     * @param query Query, TypedQuery, or StoredProcedureQuery object
     * @since Java Persistence 2.1
     */
    @Override
    public void addNamedQuery(String name, Query query) {

    }

    /**
     * Return an object of the specified type to allow access to the
     * provider-specific API. If the provider's EntityManagerFactory
     * implementation does not support the specified class, the
     * PersistenceException is thrown.
     *
     * @param cls the class of the object to be returned. This is
     *            normally either the underlying EntityManagerFactory
     *            implementation class or an interface that it implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not
     *                              support the call
     */
    @Override
    public <T> T unwrap(Class<T> cls) {
        return null;
    }

    /**
     * Add a named copy of the EntityGraph to the
     * EntityManagerFactory. If an entity graph with the same name
     * already exists, it is replaced.
     *
     * @param graphName   name for the entity graph
     * @param entityGraph entity graph
     * @since JPA 2.1
     */
    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {

    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
