package ru.otus.base;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;

@Data
public class WrapperDataSet {
    @Id
    private long id;
    private Boolean aBoolean;
    private Byte aByte;
    private Character aChar;
    private Short aShort;
    private Integer anInt;
    private Long aLong;
    private Float aFloat;
    private Double aDouble;

    public WrapperDataSet() {}

    @Builder
    private WrapperDataSet(long id, Boolean aBoolean, Byte aByte, Character aChar, Short aShort, Integer anInt, Long aLong, Float aFloat, Double aDouble) {
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
