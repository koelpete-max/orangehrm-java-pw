package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.main.SidePanelItem;
import com.example.pages.main.TopbarPanelText;
import com.example.pages.main.admin.UserRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AdminPageTest extends BaseTest {

    @BeforeMethod
    public void loginToPage() {
        navigateToHomePage(baseUrl);
        loginPage.login(defaultTestUser.username(), defaultTestUser.password());
    }

    @Test
    public void verifyAdminPageTest() {

        testLog.step("Verifying Admin Page Test");

        testLog.step("Asserting that the Admin Page is loaded");
        sidePanel.selectMenuActiveItem(SidePanelItem.ADMIN);
        Assert.assertEquals(sidePanel.getMenuActiveItemName(), SidePanelItem.ADMIN.toString());

        Assert.assertTrue(topbarPanel.getPanelText().contains(TopbarPanelText.ADMIN.toString()));

        testLog.step("Admin Page is successfully loaded");
    }

    @DataProvider(name = "userRoleData")
    public Object[][] userRoleData() {
        return new Object[][] {
                { UserRole.ADMIN },
                { UserRole.ESS },
        };
    }

    @Test(dataProvider = "userRoleData")
    public void verifyUserRolesTest(UserRole userRole) {

        testLog.step("Verifying roles on the Admin Page");

        sidePanel.selectMenuActiveItem(SidePanelItem.ADMIN);

        adminPage.selectUserRole(userRole);

        testLog.step("Asserting that the role is properly displayed");
        Assert.assertTrue(adminPage.getSelectedRoleCount(userRole),
                String.format("Failed to select role '%s'. Different roles were selected", userRole)
        );

        testLog.step("Verification for role '" + userRole.toString() + "' passed");
    }
}
