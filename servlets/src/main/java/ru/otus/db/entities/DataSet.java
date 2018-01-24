package ru.otus.db.entities;


import javax.persistence.Id;

public class DataSet {
    @Id
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
