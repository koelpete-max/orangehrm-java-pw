package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.main.SidePanelItem;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardPageTest extends BaseTest {

    @BeforeMethod
    public void loginToPage() {
        navigateToHomePage(BASE_URL);
        loginPage.login(defaultTestUser.username(), defaultTestUser.password());
    }

    @Test
    public void verifyDashboardPageTest() {
        testLog.step("Verifying Dashboard Page Test");

        testLog.step("Asserting that the Dashboard Page is loaded");
        sidePanel.selectMenuActiveItem(SidePanelItem.DASHBOARD);
        Assert.assertEquals(sidePanel.getMenuActiveItemName(),
                SidePanelItem.DASHBOARD.toString()
        );

        testLog.step("Asserting that the Dashboard page components are available");
        Assert.assertTrue(dashboardPage.isComponentPageReady(),
                "Dashboard page components not properly loaded"
        );

        testLog.step("Admin Dashboard is successfully loaded");
    }
}
