package ru.otus.base;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.OneToOne;


@Setter
@Getter
@ToString
public class PhonesDataSet extends DataSet {
    private String phone;
    private int houseNumber;

    @OneToOne
    private UserDataSet user;

    public PhonesDataSet() {}

    public PhonesDataSet(String name, int houseNumber) {
        this.phone = name; this.houseNumber = houseNumber;
    }

    public PhonesDataSet(String name) {
        this.phone = name;
    }

}
