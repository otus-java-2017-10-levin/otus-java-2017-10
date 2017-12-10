package ru.otus.json;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.StringWriter;

public class JavaXJson implements SerializeBuilder {

    JsonObjectBuilder builder = Json.createObjectBuilder();

    @Override
    public SerializeBuilder addObject(String name, FieldEntity value) throws IllegalArgumentException {

        return null;
    }

    @Override
    public SerializeBuilder addArray(String name, Object... values) throws IllegalArgumentException {
        return null;
    }

    @Override
    public String toJsonString() {
        return this.toString();
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = Json.createWriter(stringWriter);
        writer.writeObject(builder.build());
        writer.close();
        return stringWriter.getBuffer().toString();
    }
}
