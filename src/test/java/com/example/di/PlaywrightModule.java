package com.example.di;

import com.example.utils.EnvConfig;
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

        return playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--start-maximized"))
                        .setHeadless(EnvConfig.HEADLESS)
        );
    }

    @Provides
    @Singleton
    Page providePage(Browser browser) {
        return browser.newPage();
    }
}