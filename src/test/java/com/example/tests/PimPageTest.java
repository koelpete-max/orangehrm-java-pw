package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.main.SidePanelItem;
import com.example.pages.main.TopbarPanelText;
import com.example.pages.main.pim.EmployeeData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Objects;

public class PimPageTest extends BaseTest {
    String topbarStaticText;

    @BeforeMethod
    public void navigateToHomePage() {
        navigateToHomePage(BASE_URL);
        loginPage.login(defaultTestUser.username(), defaultTestUser.password());
        sidePanel.selectMenuActiveItem(SidePanelItem.PIM);
        topbarStaticText = topbarPanel.getPanelText();
    }

    @DataProvider(name = "expectedEmployeeData")
    public Object[][] expectedEmployeeData() {
        return new Object[][] {
            { new EmployeeData("0001", "Bark", "Hokga",  "") },
            { new EmployeeData("0002", "Schuurk Be", "Bolk",  "") },
        };
    }

    @Test(dataProvider = "expectedEmployeeData")
    public void employeeInformationTest(EmployeeData expectedEmployeeData) throws Exception {
        testLog.step("Verifying employee information Test");
        if (!Objects.equals(topbarStaticText, TopbarPanelText.PIM.toString())) {
            throw new Exception("PIM Panel not visible");
        }

        testLog.step("Searching employee information for "+
                expectedEmployeeData.getFirstMiddleName()+ " " +expectedEmployeeData.getLastName() +
                ", id="+expectedEmployeeData.getId()
        );

        testLog.step("Asserting employee information");
        var employeeData = pimPage.searchEmployeeByName(expectedEmployeeData.getFirstMiddleName());
        Assert.assertNotNull(employeeData, "Employee data not found");
        Assert.assertEquals(expectedEmployeeData, employeeData, "Employee data not match");

        testLog.step("Employee information successfully validated");
    }
}
