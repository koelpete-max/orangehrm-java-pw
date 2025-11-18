package com.example.pages.main.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

import javax.inject.Inject;

public class AdminPage {
    final Page page;

    final AdminPageLocators adminPageLocators;
    final Locator systemUsersLocator;
    final Locator arrowLocator;
    final Locator searchButtonLocator;

    @Inject
    public AdminPage(Page page) {

        this.page = page;
        adminPageLocators = new AdminPageLocators(page);
        systemUsersLocator = adminPageLocators.getSystemUsersLocator();
        arrowLocator = adminPageLocators.getArrowLocator();
        searchButtonLocator = adminPageLocators.getSearchButtonLocator();

    }

    public boolean isPageReady() {

        page.waitForLoadState(LoadState.NETWORKIDLE);
        return systemUsersLocator.isVisible();
    }

    public void selectUserRole(UserRole userRole) {

        arrowLocator.first().click();
        page.getByRole(AriaRole.LISTBOX).getByText(userRole.toString()).click();
        searchButtonLocator.click();
    }

    public boolean getSelectedRoleCount(UserRole userRole) {
        var locator = adminPageLocators.getCellRolesLocator(userRole.toString());
        var userRoles = locator.allTextContents();

        return userRoles.stream().allMatch(role -> role.equals(userRole.toString()));
    }
}
