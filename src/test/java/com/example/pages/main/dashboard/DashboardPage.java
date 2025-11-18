package com.example.pages.main.dashboard;

import com.microsoft.playwright.Page;

import javax.inject.Inject;

public class DashboardPage {

    final Page page;
    final String dashboardGridXpath = "//div[@class='oxd-grid-3 orangehrm-dashboard-grid']";

    @Inject
    public DashboardPage(Page page) {
        this.page = page;
    }

    public boolean isComponentPageReady() {
        return page.locator(dashboardGridXpath).isVisible();
    }
}
