package com.example.di;

import com.microsoft.playwright.*;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.Arrays;

@Module
public class PlaywrightModule {

    @Provides
    @Singleton
    Playwright providePlaywright() {
        return Playwright.create();
    }

    @Provides
    @Singleton
    Browser provideBrowser(Playwright playwright) {
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"));
        return playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--start-maximized"))
                        .setHeadless(headless)
        );
    }

    @Provides
    @Singleton
    Page providePage(Browser browser) {
        return browser.newPage();
    }
}