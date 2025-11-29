package com.example.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.example.base.TestUser;
import lombok.Getter;

@Getter
public class TestContext {

    private final Playwright playwright;
    private final Browser browser;
    private final Page page;
    private final String baseUrl;
    private final TestUser defaultTestUser;

    public TestContext(
            Playwright playwright,
            Browser browser,
            Page page,
            String baseUrl,
            TestUser defaultTestUser
    ) {
        this.playwright = playwright;
        this.browser = browser;
        this.page = page;
        this.baseUrl = baseUrl;
        this.defaultTestUser = defaultTestUser;
    }

    public void close() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}