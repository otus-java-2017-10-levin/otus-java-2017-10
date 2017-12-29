package ru.otus.base;


import lombok.Data;

import javax.persistence.*;

@Data
public class PhoneDataSet {
    @Id
    private long id;
    private String phone;
    private int houseNumber;

    public PhoneDataSet() {}

    public PhoneDataSet(String phone) {this.phone = phone;}

    public PhoneDataSet(long id, String phone) {
        this.id = id;
        this.phone = phone;
    }
}