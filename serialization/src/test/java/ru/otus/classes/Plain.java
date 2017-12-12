package ru.otus.classes;

@SuppressWarnings("FieldCanBeLocal")
public class Plain {
    private boolean aBoolean = true;
    private byte aByte = 7;
    private char aChar = '\u0001';
    private short aShort = -1;
    private int anInt;
    private float aFloat;
    private double aDouble = 1.0;

    @Override
    public String toString() {
        return "{\"aBoolean\":"+aBoolean+
            ",\"aChar\":"+aChar+
                ",\"aShort\":"+aShort+
                ",\"anInt\":"+anInt+
                ",\"aFloat\":"+aFloat+
                ",\"aDouble\":"+aDouble+
                ",\"aByte\":"+aByte+"}";
    }
}