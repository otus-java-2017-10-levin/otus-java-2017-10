package ru.otus.base;

import lombok.*;

import javax.persistence.*;

@Data
public class UserDataSet extends DataSet {
    private String name;
    private int age;


    public UserDataSet() {}

    public UserDataSet(String name) {
        this.name = name;
    }
}