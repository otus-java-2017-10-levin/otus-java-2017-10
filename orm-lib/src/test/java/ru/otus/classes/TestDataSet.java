package ru.otus.classes;

import ru.otus.base.DataSet;

import javax.persistence.Column;
import javax.persistence.OneToMany;

public class TestDataSet extends DataSet {

    @Column(name = "age")
    @OneToMany(mappedBy = "test")
    private int age;
}
