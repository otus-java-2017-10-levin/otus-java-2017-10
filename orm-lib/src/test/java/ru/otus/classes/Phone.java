package ru.otus.classes;

import lombok.*;

import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class Phone extends DataSet {

    @NonNull
    @ManyToOne
    private UserDataSet owner;

    @NonNull
    private String phone;
}
