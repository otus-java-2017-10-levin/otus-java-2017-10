package ru.otus.json;


import java.util.ArrayList;
import java.util.List;

final class SimpleArrayBuilder implements ArrayBuilder {
    private final List<Object> list = new ArrayList<>();

    @Override
    public ArrayBuilder add(Object object) {
        list.add(object);
        return this;
    }

    @Override
    public Object build() {
        return list;
    }
}
