package ru.otus.hw02.memory;


/*
        Measuring object data through garbage-collector.
 */


class GCMeasure {

    public GCMeasure() { prepare(); }
    public static final int TIMES = 3_000_000;
//    private MeasureObject mObj;
    private Object[] objects;

    private long measure() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private void runGC() {
        for (int i=0; i < 4; i++) {
            System.gc();
        }
    }

    public void prepare() {
        runGC();
        measure();
    }


    public long getObjectSize(MeasureObject obj) {

        objects = new Object[TIMES];
        runGC();
        long memStart = measure();

        for (int i = 0; i < TIMES; i++) {
            objects[i] = obj.createObject();
        }
        runGC();
        long memEnd = measure();
        return (memEnd - memStart) / TIMES;
    }
    
}
