package ru.otus.db.dao;

import ru.otus.db.entities.Phone;

import javax.persistence.EntityManager;

public class PhoneDAO extends GenericDAO<Phone> {
    public PhoneDAO(EntityManager manager) {
        super(Phone.class, manager);
    }
}