package com.example.di;

import com.example.core.TestContext;
import com.example.base.TestUser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class TestContextModule {

    @Provides
    @Singleton
    TestContext provideTestContext(
            Playwright playwright,
            Browser browser,
            Page page,
            @Named("baseUrl") String baseUrl,
            TestUser defaultTestUser
    ) {
        return new TestContext(playwright, browser, page, baseUrl, defaultTestUser);
    }
}