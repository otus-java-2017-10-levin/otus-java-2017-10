package ru.otus.base;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;


@Data
public class PrimitiveDataSet {
    @Id
    private long id;
    private boolean aBoolean;
    private byte aByte;
    private char aChar;
    private short aShort;
    private int anInt;
    private long aLong;
    private float aFloat;
    private double aDouble;

    public PrimitiveDataSet() {}

    @Builder
    private PrimitiveDataSet(long id, boolean aBoolean, byte aByte, char aChar, short aShort, int anInt, long aLong, float aFloat, double aDouble) {
        this.id = id;
        this.aBoolean = aBoolean;
        this.aByte = aByte;
        this.aChar = aChar;
        this.aShort = aShort;
        this.anInt = anInt;
        this.aLong = aLong;
        this.aFloat = aFloat;
        this.aDouble = aDouble;
    }
}