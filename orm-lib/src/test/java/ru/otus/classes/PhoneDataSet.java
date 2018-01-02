package ru.otus.classes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.base.DataSet;

import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhoneDataSet extends DataSet {

    String number;

    @ManyToOne
    EmployeeDataSet owner;
}
