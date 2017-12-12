package ru.otus.classes;

import java.util.HashSet;
import java.util.Set;

public class ComplexSet {

    private final Set<ComplexArrays> complexArrays = new HashSet<>();
    {
        complexArrays.add(new ComplexArrays());
        complexArrays.add(null);
    }
}
