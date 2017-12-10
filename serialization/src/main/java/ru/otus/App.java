package ru.otus;

import org.json.simple.JSONObject;
import ru.otus.internals.JsonDeserializer;
import ru.otus.internals.JsonFactory;
import ru.otus.internals.JsonSerializer;

import java.io.Serializable;

public class App
{
    public static void main( String[] args )
    {
        JsonSerializer json = JsonFactory.get(JsonFactory.JSON.GSON);
        Bag bag = new Bag();
        System.out.println(bag);
        bag.k = 32;
        String str = json.toJson(bag);
        System.out.println(str);
        Bag bag1 = deserialize((JsonDeserializer)json, str, Bag.class);
        System.out.println(bag1);
    }

    private static <T> T deserialize(JsonDeserializer json, String str, Class<T> cl) {
        return json.fromJson(str, cl);
    }
}

class Bag {
    String str = "abc";
    int k = 42;

    @Override
    public String toString() {
        return "str: "+ str + "; k: " + k;
    }
}