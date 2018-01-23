package ru.otus.model.entities;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class User extends DataSet {
    private String name;
    private int age;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "owner")
    private List<Phone> phones = new ArrayList<>();

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public void addPhone(@NotNull Phone phone) {
        phones.add(phone);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + (address == null ? null : address.getAddress()) +
                ", phones=" + phones +
                ", id=" + id +
                '}';
    }
}
