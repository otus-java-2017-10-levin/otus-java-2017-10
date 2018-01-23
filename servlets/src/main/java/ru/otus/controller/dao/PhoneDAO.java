package ru.otus.controller.dao;

import ru.otus.model.entities.Phone;

import javax.persistence.EntityManager;

public class PhoneDAO extends GenericDAO<Phone> {
    public PhoneDAO(EntityManager manager) {
        super(Phone.class, manager);
    }
}