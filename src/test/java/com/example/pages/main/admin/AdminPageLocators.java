package com.example.pages.main.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AdminPageLocators {

    final Page page;
    final String systemUsersClassName = "h5.oxd-text.oxd-text--h5.oxd-table-filter-title";
    final String arrowClassName = ".oxd-icon.bi-caret-down-fill.oxd-select-text--arrow";
    final String searchButtonXpath = "//button[normalize-space()='Search']";
    final String cellRolesXpath = "//*[@role='cell']/*[text()='SelectedRole']";

    public AdminPageLocators(Page page) {
        this.page = page;
    }

    public Locator getSystemUsersLocator() {
        return page.locator(systemUsersClassName);
    }

    public Locator getArrowLocator() {
        return page.locator(arrowClassName);
    }

    public Locator getSearchButtonLocator() {
        return page.locator(searchButtonXpath);
    }

    public Locator getCellRolesLocator(String selectedRole) {
        var xpath = cellRolesXpath.replace("SelectedRole", selectedRole);
        return page.locator(xpath);
    }
}
