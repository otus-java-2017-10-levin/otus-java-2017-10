package ru.otus.json;

public final class BuilderFactory {
    private BuilderFactory(){}

    public enum Builder {
        JAVAX,
        SIMPLE
    }

    public static ObjectBuilder createBuilder(Builder builder) {
        if (builder == Builder.JAVAX) {
            return new JavaXObjectBuilder();
        }
        return new SimpleObjectBuilder();
    }

    public static ArrayBuilder createArrayBuilder(Builder builder) {
        if (builder == Builder.JAVAX) {
            return new JavaXArrayBuilder();
        }
        return new SimpleArrayBuilder();
    }
}
