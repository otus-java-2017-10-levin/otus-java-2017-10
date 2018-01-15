package ru.otus.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

class MyEntityTransaction implements EntityTransaction {

    private final EntityManager entityManager;
    private boolean isActive;
    private boolean isRollbackOnly = false;

    MyEntityTransaction(EntityManager entityManager) {
        this.entityManager = entityManager;
        isActive = false;
    }

    @Override
    public void begin() {
        if (isActive())
            throw new IllegalStateException("transaction has already begun");

        isActive = true;
    }

    @Override
    public void commit() {
        checkIsActive();

        entityManager.flush();
        isActive = false;
    }

    @Override
    public void rollback() {
        checkIsActive();

        entityManager.clear();
    }

    @Override
    public void setRollbackOnly() {
        checkIsActive();

        isRollbackOnly = true;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean getRollbackOnly() {
        checkIsActive();
        return isRollbackOnly;
    }

    private void checkIsActive() {
        if (!isActive())
            throw new IllegalStateException("transaction is not active");
    }
}
