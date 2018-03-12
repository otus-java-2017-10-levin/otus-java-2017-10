package ru.otus.db.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.otus.db.entities.Address;
import ru.otus.db.entities.Phone;
import ru.otus.db.entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserTypeAdapter extends TypeAdapter<User> {

    @SuppressWarnings("SameParameterValue")
    private void writeSafeNull(JsonWriter out, String name, String value) throws IOException {
        if (name == null) {
            out.nullValue();
        } else {
            out.name(name).value(value);
        }
    }


    @Override
    public void write(JsonWriter out, User value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("id").value(value.getId());
        writeSafeNull(out, "name", value.getName());

        out.name("age").value(value.getAge());
        out.name("address");
        writeAddress(out, value.getAddress());
        out.name("phones");
        out.beginArray();
        final List<Phone> phones = value.getPhones();
        if (phones != null) {
            for (Phone phone : phones) {
                writePhone(out, phone);
            }
        }
        out.endArray();
        out.endObject();
    }

    private void writePhone(JsonWriter out, Phone phone) throws IOException {
        out.beginObject();
        if (phone != null) {
            out.name("id").value(phone.getId());
            out.name("phone").value(phone.getPhone());
        }
        out.endObject();
    }

    private void writeAddress(JsonWriter out, Address address) throws IOException {
        out.beginObject();
        if (address != null) {
            out.name("id").value(address.getId());
            out.name("address").value(address.getAddress());
        }
        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }
        User user = new User();

        in.beginObject();
        while (in.hasNext()) {
            final String name = in.nextName();
            if ("id".equals(name)) {
                user.setId(in.nextLong());
            } else if ("age".equals(name)) {
                user.setAge(in.nextInt());
            } else if ("address".equals(name)) {
                user.setAddress(saveAddress(in, user));
            } else if ("phones".equals(name) && in.peek() != JsonToken.NULL) {
                user.setPhones(savePhones(in, user));
            } else if ("name".equals(name)) {
                user.setName(in.nextString());
            }
        }
        in.endObject();
        return user;
    }

    private List<Phone> savePhones(JsonReader in, User user) throws IOException {
        List<Phone> list = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()) {
            list.add(savePhone(in, user));
        }
        in.endArray();
        return list;
    }

    private Phone savePhone(JsonReader in, User user) throws IOException {
        final Phone phone = new Phone();

        in.beginObject();
        while (in.hasNext()) {
            final String name = in.nextName();
            if ("id".equals(name)) {
                phone.setId(in.nextLong());
            } else if ("phone".equals(name)) {
                phone.setPhone(in.nextString());
            }
        }
        in.endObject();
        phone.setOwner(user);
        return phone;
    }

    private Address saveAddress(JsonReader in, User user) throws IOException {
        final Address address = new Address();
        address.setUser(user);
        in.beginObject();
        while (in.hasNext()) {
            final String name = in.nextName();
            if ("id".equals(name)) {
                address.setId(in.nextLong());
            } else if ("address".equals(name)) {
                address.setAddress(in.nextString());
            }
        }
        in.endObject();
        return address;
    }
}