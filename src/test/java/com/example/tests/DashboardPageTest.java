package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.main.SidePanelItem;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardPageTest extends BaseTest {

    @BeforeMethod
    public void loginToPage() {
        navigateToHomePage(baseUrl);
        loginPage.login(defaultTestUser.username(), defaultTestUser.password());
    }

    @Test
    public void verifyDashboardPageTest() {
        tlog.step("Verifying Dashboard Page Test");

        tlog.step("Asserting that the Dashboard Page is loaded");
        sidePanel.selectMenuActiveItem(SidePanelItem.DASHBOARD);
        Assert.assertEquals(sidePanel.getMenuActiveItemName(),
                SidePanelItem.DASHBOARD.toString()
        );

        tlog.step("Asserting that the Dashboard page components are available");
        Assert.assertTrue(dashboardPage.isComponentPageReady(),
                "Dashboard page components not properly loaded"
        );

        tlog.step("Admin Dashboard is successfully loaded");
    }
}
