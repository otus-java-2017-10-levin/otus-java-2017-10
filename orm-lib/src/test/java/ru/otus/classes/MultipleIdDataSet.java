package ru.otus.classes;

import ru.otus.base.DataSet;

import javax.persistence.Id;

public class MultipleIdDataSet extends DataSet {

    @Id
    private long id2;
}
