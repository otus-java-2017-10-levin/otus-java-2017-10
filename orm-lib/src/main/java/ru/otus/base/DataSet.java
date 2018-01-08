package ru.otus.base;

import javax.persistence.*;

public abstract class DataSet {
    @Id
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
