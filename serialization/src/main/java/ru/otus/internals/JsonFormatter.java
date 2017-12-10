package ru.otus.internals;

public class JsonFormatter extends JsonDecorator {

    public JsonFormatter(JsonSerializer serializer) {
        super(serializer);
    }

    @Override
    public String toJson(Object object) {
        String res = super.toJson(object);
        return GsonSerializer.formatJson(res);
    }
}
