package ru.otus.leaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashLeak implements Leak {

    private class AbstractObject {
        final Integer i;
        final List<AbstractObject> objs;

        AbstractObject(Integer i) {
            this.i = i;
             objs = new ArrayList<>();
        }

        @Override
        public int hashCode() {
            return i.hashCode();
        }
    }

    private final Map<AbstractObject, String> hashes = new HashMap<>();

    @Override
    public void execute(int n) throws OutOfMemoryError {
        for (int i=0; i < n; i++) {
            if (!hashes.containsKey(new AbstractObject(i))) {
                hashes.put(new AbstractObject(i), "num: " + i);
            }
        }
    }
}
