package ru.otus.sort;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {

    public static int[] generateArray(int size) {
        final List<Integer> collect = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(collect);

        return collect.stream().mapToInt(Integer::intValue).toArray();
    }

    public static Integer[] generateObjectArray(int size) {
        final List<Integer> collect = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(collect);

        return collect.toArray(new Integer[0]);
    }

}
