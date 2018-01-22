package ru.otus.classes;

import javax.persistence.*;

public abstract class DataSet {
    @Id
    long id;

    public long getId() {
        return id;
    }
}
