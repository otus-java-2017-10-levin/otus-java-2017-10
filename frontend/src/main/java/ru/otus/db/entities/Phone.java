package ru.otus.db.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
public class Phone extends DataSet {

    @NonNull
    @ManyToOne
    private User owner;

    @NonNull
    private String phone;

    @Override
    public String toString() {
        return "Phone{" +
                "owner=" + (owner == null ? null : owner.getName()) +
                ", phone='" + phone + '\'' +
                ", id=" + id +
                '}';
    }
}