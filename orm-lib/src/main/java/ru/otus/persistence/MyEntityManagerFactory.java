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
    private boolean isOpen = true;
    private final String persistenceUnit;

    MyEntityManagerFactory(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    @Override
    public EntityManager createEntityManager() {
        String XML = "META-INF/persistence.xml";
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

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
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
    public void close() {
        managers.forEach(EntityManager::close);
        isOpen = false;
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cache getCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addNamedQuery(String name, Query query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        throw new PersistenceException();
    }

    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
