package ru.otus.classes;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
@Data
public class ComplexSet {

    private Set<ComplexArrays> complexArrays;

    public void init() {
        complexArrays = new HashSet<>();
        complexArrays.add(new ComplexArrays());
        complexArrays.add(null);
    }
}
