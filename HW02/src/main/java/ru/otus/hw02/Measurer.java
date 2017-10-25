package ru.otus.hw02;

public class Measurer {

    /**
     * Return estimate object size by instrumentation
     * @param obj - object to measure
     * @return size in bytes
     */

    public static long getObjectSize(Object obj) { return MeasureAgent.deepSizeOfObject(obj); }

    public static long getShallowSize(Object obj) {
        return MeasureAgent.shallowSizeOfObject(obj);
    }
}
