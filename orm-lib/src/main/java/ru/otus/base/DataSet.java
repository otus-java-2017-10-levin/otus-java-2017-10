package ru.otus.base;

import lombok.Data;

import javax.persistence.Id;

@Data
public abstract class DataSet {
    @Id
    private long id;
}
