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

    private final double defaultTimeout = 10_000;

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
        return isInvalidCredentialsMessageVisible(defaultTimeout);
    }

    public boolean isInvalidCredentialsMessageVisible(double timeout) {

        page.setDefaultTimeout(timeout);
        page.waitForCondition(() -> invalidCredentialsClassNameLocator.isVisible());
        boolean visible = invalidCredentialsClassNameLocator.isVisible();
        log.info("Checking if invalid credentials message is visible: {}", visible);
        page.setDefaultTimeout(defaultTimeout);
        return visible;
    }
}
