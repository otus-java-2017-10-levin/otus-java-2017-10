package ru.otus.base;

import lombok.Data;

import javax.persistence.*;

@Data
public class UsersDataSet {
    @Id
    private long id;
    private String name;

    public UsersDataSet() {}

    public UsersDataSet(long id, String name) {
        this.id = id;
        this.name = name;
    }
}