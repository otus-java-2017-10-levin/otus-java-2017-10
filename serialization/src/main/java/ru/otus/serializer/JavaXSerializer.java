package ru.otus.serializer;


import ru.otus.json.BuilderFactory;

class JavaXSerializer extends AbstractSerializer {

    public JavaXSerializer() {
        super(BuilderFactory.Builder.JAVAX);
    }
}
