package com.example.utils;

import com.example.base.TestUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.IOException;

@Slf4j
public class TestUserProvider {

    private static JsonNode root;
    private static final Object lock = new Object();

    private final static String usernameText = "username";
    private final static String passwordText = "password";

    public final static String ADMIN_TYPE_USER = "ADMIN";
    public final static String HACKER_TYPE_USER = "HACKER";

    private static void init() throws IOException {
        if (root == null) {
            synchronized (lock) {
                if (root == null) {
                    try (InputStream is =
                                 TestUserProvider.class.getClassLoader()
                                         .getResourceAsStream("config/test-users.json")) {
                        if (is == null) {
                            throw new IllegalStateException("test-users.json not found");
                        }
                        ObjectMapper mapper = new ObjectMapper();
                        root = mapper.readTree(is);
                    }
                }
            }
        }
    }

    public static TestUser getDefaultUser() {
        return getUser(ADMIN_TYPE_USER);
    }

    public static TestUser getUser(String username) {

        try {
            init();
            JsonNode adminNode = root.get(username);
            var user = new TestUser(
                    adminNode.get(usernameText).asText(),
                    adminNode.get(passwordText).asText()
            );
            log.info("Default TestUser: '{}'", user.username());
            return user;
        } catch (IOException e) {
            log.error("Error reading test-users.json", e);
            return null;
        }
    }
}