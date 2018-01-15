package ru.otus.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class MyEntityTransaction implements EntityTransaction {

    private EntityManager entityManager;
    private boolean isActive;
    private boolean isRollbackOnly = false;

    MyEntityTransaction(EntityManager entityManager) {
        this.entityManager = entityManager;
        isActive = false;
    }

    /**
     * Start a resource transaction.
     *
     * @throws IllegalStateException if <code>isActive()</code> is true
     */
    @Override
    public void begin() {
        if (isActive())
            throw new IllegalStateException("transaction has already begun");

        isActive = true;
    }

    /**
     * Commit the current resource transaction, writing any
     * unflushed changes to the database.
     *
     * @throws IllegalStateException if <code>isActive()</code> is false
     * @throws RollbackException     if the commit fails
     */
    @Override
    public void commit() {
        checkIsActive();

        entityManager.flush();
        isActive = false;
    }

    /**
     * Roll back the current resource transaction.
     *
     * @throws IllegalStateException if <code>isActive()</code> is false
     * @throws PersistenceException  if an unexpected error
     *                               condition is encountered
     */
    @Override
    public void rollback() {
        checkIsActive();

        entityManager.clear();
    }

    private void checkIsActive() {
        if (!isActive())
            throw new IllegalStateException("transaction is not active");
    }

    /**
     * Mark the current resource transaction so that the only
     * possible outcome of the transaction is for the transaction
     * to be rolled back.
     *
     * @throws IllegalStateException if <code>isActive()</code> is false
     */
    @Override
    public void setRollbackOnly() {
        checkIsActive();

        isRollbackOnly = true;
    }

    /**
     * Determine whether the current resource transaction has been
     * marked for rollback.
     *
     * @return boolean indicating whether the transaction has been
     * marked for rollback
     * @throws IllegalStateException if <code>isActive()</code> is false
     */
    @Override
    public boolean getRollbackOnly() {
        checkIsActive();
        return isRollbackOnly;
    }

    /**
     * Indicate whether a resource transaction is in progress.
     *
     * @return boolean indicating whether transaction is
     * in progress
     * @throws PersistenceException if an unexpected error
     *                              condition is encountered
     */
    @Override
    public boolean isActive() {
        return isActive;
    }
}
