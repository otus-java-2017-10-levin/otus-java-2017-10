package ru.otus.internals;

import ru.otus.json.JsonBuilder;

import java.util.Map;

class SimpleJsonSerializer extends AbstractSerializer {

    SimpleJsonSerializer() {
        super(JsonBuilder.getBuilder(JsonBuilder.JSON.SIMPLE));
    }

    @Override
    protected void addMapToBuilder(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            builder.addObject(entry.getKey(), entry.getValue());
        }
    }
}
