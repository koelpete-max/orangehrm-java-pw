package com.example.pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public record LoginPageLocators(Page page) {

    public Locator getUserNameLocator() {
        String usernameTextbox = "input[name='username']";
        return page.locator(usernameTextbox);
    }

    public Locator getPasswordLocator() {
        String passwordTextbox = "input[name='password']";
        return page.locator(passwordTextbox);
    }

    public Locator getLoginButtonLocator() {
        String loginButton = "button[type='submit']";
        return page.locator(loginButton);
    }

    public Locator getInvalidCredentialsLocator() {
        String invalidCredentialsClassName = "p.oxd-text.oxd-text--p.oxd-alert-content-text";
        return page.locator(invalidCredentialsClassName);
    }
}
