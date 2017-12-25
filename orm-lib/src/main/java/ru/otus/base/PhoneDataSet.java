package ru.otus.base;


import javax.persistence.*;

public class PhoneDataSet {
    @Id
    private long id;
    private String phone;

    public PhoneDataSet() {}

    public PhoneDataSet(long id, String phone) {
        this.id = id;
        this.phone = phone;
    }
}