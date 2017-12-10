package ru.otus.json;

public class PlainWrappers {

        Boolean aBoolean = true;
        Byte aByte = 7;
        Character aChar;
        Short aShort = -1;
        Integer anInt;
        Float aFloat;
        Double aDouble = 1.0;
        Long aLong = 2L;

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
