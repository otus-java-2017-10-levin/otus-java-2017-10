package ru.otus.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthUtil {

    private final static Map<String, String> users = new HashMap<>();
    private final static Set<String> hashes = new HashSet<>();

    static {
        users.put("sa", "");
    }

    public static boolean isValidUser(String userName, String pass) {
        if (userName == null || pass == null)
            throw new IllegalArgumentException();
        return pass.equals(users.get(userName));
    }

    public static String generateHash(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String str: args) builder.append(str);

        hashes.add(builder.toString());
        return builder.toString();
    }

    public static boolean validateHash(String hash) {
        return hashes.contains(hash);
    }
}
