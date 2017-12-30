package ru.otus.base;

import lombok.Data;

import javax.persistence.Id;

@Data
public class PhoneDataSet extends DataSet {
    private String phone;
    private int houseNumber;


    public PhoneDataSet() {}

    public PhoneDataSet(String name, int houseNumber) {
        this.phone = name; this.houseNumber = houseNumber;
    }

    public PhoneDataSet(String name) {
        this.phone = name;
    }

}
