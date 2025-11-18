package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader()
                .getResourceAsStream("config/test-config.properties")) {

            if (input == null) {
                throw new RuntimeException("Config file not found!");
            }

            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load test-config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}