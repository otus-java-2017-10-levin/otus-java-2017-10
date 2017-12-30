package ru.otus.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MyEntityManagerFactory implements EntityManagerFactory {

    private final List<EntityManager> managers = new ArrayList<>();
    private boolean isOpen = true;

    public MyEntityManagerFactory() {
    }

    @Override
    public EntityManager createEntityManager() {
        return createEntityManager(null);
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        EntityManager manager = new MyEntityManager(map);
        managers.add(manager);
        return manager;
    }

    @Override
    public void close() {
        managers.forEach(EntityManager::close);
        isOpen = false;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
