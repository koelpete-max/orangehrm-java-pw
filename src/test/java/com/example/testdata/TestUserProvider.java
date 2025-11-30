package com.example.testdata;

import com.example.base.TestUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestUserProvider {

    private static final Map<String, TestUser> CACHE = new ConcurrentHashMap<>();

    static {
        try {
            InputStream is = TestUserProvider.class
                    .getClassLoader()
                    .getResourceAsStream("config/test-users.json");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            root.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode node = entry.getValue();
                CACHE.put(key, new TestUser(
                        node.get("username").asText(),
                        node.get("password").asText(),
                        node.get("role").asText()
                ));
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test-users.json", e);
        }
    }

    public static TestUser get(String key) {
        TestUser user = CACHE.get(key);
        if (user == null) {
            throw new IllegalArgumentException("No TestUser for key: " + key);
        }
        return user;
    }

    public static TestUser defaultAdmin() {
        return get("ADMIN");
    }
}