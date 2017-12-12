package ru.otus.serializer;

import ru.otus.json.BuilderFactory;

class SimpleSerializer extends AbstractSerializer {

    public SimpleSerializer() {
        super(BuilderFactory.Builder.SIMPLE);
    }
}
