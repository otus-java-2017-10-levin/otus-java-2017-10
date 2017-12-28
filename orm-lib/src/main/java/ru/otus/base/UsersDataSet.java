package ru.otus.base;

import lombok.*;

import javax.persistence.*;

@Data
public class UsersDataSet {
    @Id
    private long id;
    private String name;

    public UsersDataSet() {}

    public UsersDataSet(String name) {
        this.name = name;
    }
}