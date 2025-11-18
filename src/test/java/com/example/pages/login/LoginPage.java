package com.example.pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class LoginPage {
    private final Page page;

    private final Locator usernameEditLocator;
    private final Locator passwordEditLocator;
    private final Locator loginButtonLocator;
    private final Locator invalidCredentialsClassNameLocator;

    @Inject
    public LoginPage(Page page) {
        this.page = page;
        var loginPageLocators = new LoginPageLocators(page);
        usernameEditLocator = loginPageLocators.getUserNameLocator();
        passwordEditLocator = loginPageLocators.getPasswordLocator();
        loginButtonLocator = loginPageLocators.getLoginButtonLocator();
        invalidCredentialsClassNameLocator = loginPageLocators.getInvalidCredentialsLocator();
    }

    public void addUsername(String username) {
        log.info("Trying to login with username '{}'", username);
        usernameEditLocator.fill(username);
    }

    public void addPassword(String password) {
        passwordEditLocator.fill(password);
    }

    public void clickLoginButton() {
        log.info("Submitting login button");
        loginButtonLocator.click();
    }

    public void login(String username, String password) {
        addUsername(username);
        addPassword(password);
        clickLoginButton();
    }

    public boolean isInvalidCredentialsMessageVisible() {
        final boolean[] visible = new boolean[1];
        page.waitForCondition(() -> {
            visible[0] = invalidCredentialsClassNameLocator.isVisible();
            return visible[0];
        });

        log.info("Checking if invalid credentials message is visible: {}", visible[0]);
        return visible[0];
    }
}
