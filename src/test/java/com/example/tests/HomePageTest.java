package com.example.tests;

import com.example.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTests extends BaseTest
{

    @Test
    public void VerifyHomePageTitleTest() {
        page.navigate("http://localhost:9080/");

        Assert.assertEquals(page.title(), "OrangeHRM");
    }
}
