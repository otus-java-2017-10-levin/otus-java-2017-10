package ru.otus.utils;

import org.jetbrains.annotations.Nullable;

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

    public static boolean isValidUser(@Nullable String userName, @Nullable String pass) {
        return userName != null && pass != null && pass.equals(users.get(userName));
    }

    public static String generateHash(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String str: args) builder.append(str);

        hashes.add(builder.toString());
        return builder.toString();
    }

    public static boolean validateHash(String hash) {
        return hash != null && hashes.contains(hash);
    }

    private static void deleteHash(String hash) {
        if (hash != null)
            hashes.remove(hash);
    }

    public static void logout(String userHash) {
        if (AuthUtil.validateHash(userHash)) {
            AuthUtil.deleteHash(userHash);
        }
    }
}
