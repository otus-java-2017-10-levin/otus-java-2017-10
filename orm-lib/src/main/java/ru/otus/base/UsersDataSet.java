package ru.otus.base;

import javax.persistence.*;

@Table
public class UsersDataSet {
    private final long id;
    private final String name;

    public UsersDataSet(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "USER_ID")
    public long getId() {
        return id;
    }

    @Column(name = "USER_NAME")
    public String getName() {
        return name;
    }
}
