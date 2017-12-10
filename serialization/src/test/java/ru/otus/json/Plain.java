package ru.otus.json;

public class Plain {
    boolean aBoolean = true;
    byte aByte = 7;
    char aChar = '\u0001';
    short aShort = -1;
    int anInt;
    float aFloat;
    double aDouble = 1.0;

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