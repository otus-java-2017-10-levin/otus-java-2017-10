package ru.otus.db.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Setter
@Getter
@Entity
public class Address extends DataSet {
    private String address;

    @OneToOne
    private User user;

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