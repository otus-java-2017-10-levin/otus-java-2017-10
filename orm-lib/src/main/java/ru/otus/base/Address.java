package ru.otus.base;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


@Setter
@Getter
@Entity
public class Address extends DataSet {
    private String address;

    @OneToOne
    private UserDataSet user;

    public Address() {}

    public Address(String name) {
        this.address = name;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", user=" + (user == null ? null : user.getName()) +
                ", id=" + id +
                '}';
    }
}
