package ru.otus.classes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.base.DataSet;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressDataSet extends DataSet {
    String city;
    String street;
    PhoneDataSet phone;
}
