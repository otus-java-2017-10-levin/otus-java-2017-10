package ru.otus.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhonesDataSet extends DataSet {
    private String phone;
    private int houseNumber;


    public PhonesDataSet() {}

    public PhonesDataSet(String name, int houseNumber) {
        this.phone = name; this.houseNumber = houseNumber;
    }

    public PhonesDataSet(String name) {
        this.phone = name;
    }

}
