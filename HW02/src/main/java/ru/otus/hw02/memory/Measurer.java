package ru.otus.hw02.memory;

public class Measurer {

    private static GCMeasure gcMeasure = new GCMeasure();
    public enum MeasureType{
        INSTRUMENTATION,
        GARBAGE_COLLECTOR
    }

    public static long getObjectSize(MeasureObject obj, MeasureType type) {
        long result = 0;
        switch (type) {
            case INSTRUMENTATION:
                result = MeasureAgent.deepSizeOfObject(obj.createObject());
                break;
            case GARBAGE_COLLECTOR:
                result = gcMeasure.getObjectSize(obj);
                break;
        }
        return result;
    }
}
