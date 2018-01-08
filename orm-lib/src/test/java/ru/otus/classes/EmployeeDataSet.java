package ru.otus.classes;

import lombok.Data;
import ru.otus.base.DataSet;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
public class EmployeeDataSet extends DataSet {

    @OneToOne
    private AddressDataSet address;

    @OneToMany(mappedBy = "owner")
    private List<PhoneDataSet> phones;
}
