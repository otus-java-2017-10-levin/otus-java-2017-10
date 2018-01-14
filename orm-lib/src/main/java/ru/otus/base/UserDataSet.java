package ru.otus.base;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserDataSet extends DataSet {
    private String name;
    private int age;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "owner")
    private List<Phone> phones = new ArrayList<>();

    public UserDataSet() {}

    public UserDataSet(String name) {
        this.name = name;
    }

    public void addPhone(@NotNull Phone phone) {
        phones.add(phone);
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                ", id=" + id +
                '}';
    }
}