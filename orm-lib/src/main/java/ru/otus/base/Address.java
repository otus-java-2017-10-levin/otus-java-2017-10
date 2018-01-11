package ru.otus.base;

import lombok.*;

import javax.persistence.OneToOne;


@Setter
@Getter
@ToString
public class Address extends DataSet {
    private String address;

    @OneToOne
    private UserDataSet user;

    public Address() {}

    public Address(String name) {
        this.address = name;
    }
}
