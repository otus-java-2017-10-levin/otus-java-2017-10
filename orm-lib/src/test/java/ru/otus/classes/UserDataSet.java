package ru.otus.classes;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
public class UserDataSet extends DataSet {
    private String name;
    private int age;

    @OneToOne
    private Address address;

    public UserDataSet() {}

    public UserDataSet(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDataSet:[id: "+id+"; name: "+name+"; age: "+age+"]";
    }
}