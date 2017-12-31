package ru.otus.base;

import lombok.Data;

import javax.persistence.*;

@Data
public abstract class DataSet {
    @Id
    private long id;
}
