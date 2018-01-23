package ru.otus.controller.dao;

import ru.otus.model.entities.Phone;
import ru.otus.model.entities.User;

import javax.persistence.EntityManager;

public class UserDAO extends GenericDAO<User> {

    public UserDAO(EntityManager manager) {
        super(User.class, manager);
    }

    public void save(User user) {
        manager.getTransaction().begin();
        manager.persist(user.getAddress());
        manager.persist(user);
        for (Phone ph : user.getPhones()) {
            manager.persist(ph);
        }
        manager.getTransaction().commit();
    }
}