package ru.otus.classes;

import javax.persistence.Column;
import javax.persistence.OneToMany;

@SuppressWarnings("unused")
public class TestDataSet extends DataSet {

    @Column(name = "age")
    @OneToMany(mappedBy = "test")
    private int age;

    @Column(name = "city")
    private String city;
}
