package ru.otus.leaks;

import java.util.HashMap;
import java.util.Map;

public class HashLeak implements Leak {

    private class AbstractObject {
        Integer i;

        AbstractObject(Integer i) {
            this.i = i;
        }

        @Override
        public int hashCode() {
            return i.hashCode();
        }
    }

    private Map<AbstractObject, String> hashes = new HashMap<>();

    @Override
    public void execute(int n) {
        for (int i=0; i < n; i++) {
            if (!hashes.containsKey(new AbstractObject(i))) {
                hashes.put(new AbstractObject(i), "num: " + i);
            }
        }
    }
}
