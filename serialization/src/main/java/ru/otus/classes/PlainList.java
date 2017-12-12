package ru.otus.classes;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
@Data
public class PlainList {
    private List<Integer> ints;

    public void init() {
        ints = Arrays.asList(1, 2, 3, 4);
    }
}
