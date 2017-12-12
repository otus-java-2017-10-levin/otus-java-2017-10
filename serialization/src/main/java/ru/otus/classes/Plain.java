package ru.otus.classes;

import lombok.Data;

@Data
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Plain {
    private boolean aBoolean;
    private byte aByte;
    private char aChar;
    private short aShort;
    private int anInt;
    private float aFloat;
    private double aDouble;

    public void init() {
        aBoolean = true;
        aByte = 7;
        aChar = '\u0001';
        aShort = -1;
        aDouble = 1.0;
    }
}