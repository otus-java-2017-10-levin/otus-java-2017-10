package ru.otus.leaks;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  StringLeak models memory execute when working with strings
 *  Implements {#link @Leak} to realise Strategy pattern
 */

class StringLeak implements Leak {

    private final int HUGE_NUMBER = 1_000_000;
    private final int SUBSTRING_TO = 5;

    private List<String> strings = new ArrayList<>();

    /*
        Using intern for get OutOfMenory: Metaspace
     */
    private String generateHugeString(@NotNull int len) {
        return Stream.generate(Math::random).limit(len).map(p -> (Math.round(p*9))).map(p -> p.toString()).collect(Collectors.joining()).intern();
    }

    /**
     * String memory execute.
     * We get huge String and substring first 5 letters.
     *
     */
    @Override
    public void execute(int times) {
        for (int i=0; i < times; i++) {
            String str = generateHugeString(HUGE_NUMBER);
            strings.add(str.substring(0, SUBSTRING_TO));
        }
    }
}
