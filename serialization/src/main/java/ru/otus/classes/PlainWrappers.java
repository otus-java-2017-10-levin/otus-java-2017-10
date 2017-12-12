package ru.otus.classes;

import lombok.Data;

@SuppressWarnings("ALL")
@Data
public class PlainWrappers {

    private Boolean aBoolean;
    private Byte aByte;
    private Character aChar;
    private Short aShort;
    private Integer anInt;
    private Float aFloat;
    private Double aDouble;
    private Long aLong;

    public void init() {
        aBoolean = true;
        aByte = 7;
        aShort = -1;
        aDouble = 1.0;
        aLong = 2L;
    }
}
