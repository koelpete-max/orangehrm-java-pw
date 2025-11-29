package com.example.utils;

import com.example.base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.IOException;

@Slf4j
public class TestUserProvider {

    private static JsonNode root;
    private static final Object lock = new Object();

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

    public static BaseTest.TestUser getDefaultAdmin() throws IOException {
        init();
        JsonNode adminNode = root.get("Admin");
        var user = new BaseTest.TestUser(
                adminNode.get("username").asText(),
                adminNode.get("password").asText()
        );
        log.info("Default TestUser: '{}'", user.username());
        return user;
    }
}