package ru.otus.classes;

import lombok.Data;

@SuppressWarnings("FieldCanBeLocal")
@Data
public class PlainArrays {
    private int anInt;
    private Long[] longs;


    public void init() {
        anInt = 10;
        longs = new Long[]{1L, 2L, 3L};

    }
}
