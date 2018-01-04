package ru.otus.base;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDataSet extends DataSet {
    private String name;
    private int age;
    private long employeeId;


    public UserDataSet() {}

    public UserDataSet(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDataSet:[id: "+id+"; name: "+name+"; age: "+age+"; employeeId: "+employeeId+"]";
    }
}