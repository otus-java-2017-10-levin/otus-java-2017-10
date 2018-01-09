package ru.otus.persistence;

import ru.otus.persistence.annotations.AnnotatedClass;

//alter table UserDataSet add constraint FKhqati05jw18942yayxofpgh2y foreign key (address_id) references AddressDataSet
public interface Constraint {

    /**
     * Gets tanle of constraint
     * @return - class represent of a table
     */
    AnnotatedClass getTable();

    /**
     * Get row name of a constraint
     * @return - String name of a row
     */
    String getFieldName();


    /**
     * Get class of a foreign table
     * @return - class represent of a foreign table
     */
    AnnotatedClass getForeignTable();

}