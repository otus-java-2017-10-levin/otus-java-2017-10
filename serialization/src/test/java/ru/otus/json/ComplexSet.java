package ru.otus.json;

import java.util.HashSet;
import java.util.Set;

public class ComplexSet {

    private Set<ComplexArrays> complexArrays = new HashSet<>();
    {
        complexArrays.add(new ComplexArrays());
        complexArrays.add(null);
    }
}
