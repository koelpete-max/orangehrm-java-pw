package com.example.core;

import com.example.pages.home.HomePage;
import com.example.pages.login.LoginPage;
import com.example.pages.main.SidePanel;
import com.example.pages.main.TopbarPanel;
import com.example.pages.main.admin.AdminPage;
import com.example.pages.main.pim.PimPage;
import com.microsoft.playwright.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestContext {

    private final Playwright playwright;
    private final Browser browser;

    @Getter
    private Page page;

    public TestContext() {
        this.playwright = Playwright.create();
        this.browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    public Page newPage() {
        BrowserContext context = browser.newContext();
        return context.newPage();
    }

    public HomePage newHomePage() {
        page = newPage();
        return new HomePage(page);
    }

    public LoginPage createLoginPage(Page page) {
        return new LoginPage(page);
    }

    public TopbarPanel createTopbarPanel(Page page) {
        return new TopbarPanel(page);
    }

    public SidePanel createSidePanel(Page page) {
        return new SidePanel(page);
    }

    public AdminPage createAdminPage(Page page) {
        return new AdminPage(page);
    }

    public PimPage createPimPage(Page page) {
        return new PimPage(page);
    }

    public void close() {
        browser.close();
        playwright.close();
    }
}