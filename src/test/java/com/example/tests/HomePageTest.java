package com.example.tests;

import com.example.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {
    @Test
    public void verifyHomePageTest() {
        testLog.step("Verifying Home Page Test");
        navigateToHomePage(baseUrl);

        testLog.step("Asserting that the page is loaded");
        Assert.assertTrue(
                homePage.getActualPageTitle().contains(homePage.getExpectedPageTitle()),
                "Unexpected title: " + homePage.getActualPageTitle());

        testLog.step("Home Page is successfully loaded");
    }
}