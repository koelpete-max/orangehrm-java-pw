package com.example.utils;

public class EnvConfig {

    public static final boolean HEADLESS = Boolean.parseBoolean(
            System.getenv().getOrDefault("HEADLESS", "false")
    );

    public static String resolveBaseUrl() {

        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = TestConfig.get("BASE_URL");
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("BASE_URL is not set");
        }
        return baseUrl;
    }

}
