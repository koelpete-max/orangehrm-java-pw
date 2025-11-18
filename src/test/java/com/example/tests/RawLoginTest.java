package com.example.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RawLoginTest {

    @Test(enabled = false)
    public void ValidLoginTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill("Admin");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).press("Tab");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("admin123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
            assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Dashboard"))).isVisible();
            page.click("p.oxd-userdropdown-name"); // I changed t
            page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Logout")).click();
            assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"))).isVisible();
            browser.close();
        }
    }
}