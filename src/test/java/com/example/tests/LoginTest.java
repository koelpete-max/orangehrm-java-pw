package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.main.SidePanelItem;
import com.example.pages.main.TopbarPanelText;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Slf4j
public class LoginTest extends BaseTest {

    @BeforeMethod
    public void startPage() {
        navigateToHomePage(baseUrl);
    }

    @Test
    public void validCredentialsShouldGrantUserAccessToTheSystemTest() {
        testLog.step("Checking login with valid credentials");
        loginPage.login(defaultTestUser.username(), defaultTestUser.password());

        testLog.step("Asserting that user is logged in");
        Assert.assertEquals(topbarPanel.getPanelText(), TopbarPanelText.DASHBOARD.toString());
        Assert.assertEquals(sidePanel.getMenuActiveItemName(), SidePanelItem.DASHBOARD.toString());

        testLog.step("User is logged in");
    }

    @Test
    public void invalidCredentialsShouldNotAllowUserToAccessTheSystemTest() {
        testLog.step("Login with invalid username and password");
        loginPage.login(defaultTestUser.username(), "******");

        testLog.step("Asserting that user is NOT logged in");
        Assert.assertTrue(loginPage.isInvalidCredentialsMessageVisible(),
                "Failed to prevent user to log in with invalid username and password");

        testLog.step("User could NOT log in");
    }

    @Test
    public void loginTestShouldBeSkipped() {
        test.skip("Skipping login test 3");
        throw new SkipException("Skipping this test");
    }
}
