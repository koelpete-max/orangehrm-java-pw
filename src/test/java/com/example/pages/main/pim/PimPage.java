package com.example.pages.main.pim;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class PimPage {

    final Page page;
    final PimPageLocators pimPageLocators;
    final Locator usernameEditLocator;
    final Locator searchButtonLocator;
    final Locator oneRecordFoundLocator;
    final Locator userRowLocator;

    final String employeeNameFieldText = "Type for hints...";
    final String searButtonXpath = "[type='submit']";
    final String cellFirstMiddleNameXpath = "//div[contains(text(),'FirstMiddleName')]";

    @Inject
    public PimPage(Page page) {

        this.page = page;
        pimPageLocators = new PimPageLocators(page);
        usernameEditLocator = pimPageLocators.getUserameEditLocator();
        searchButtonLocator = pimPageLocators.getSearchButtonLocator();
        oneRecordFoundLocator = pimPageLocators.getOneRecordFoundTextLocator();
        userRowLocator = pimPageLocators.getUserRowLocator();
    }

    public void enterEmployeeName(String employeeName) {
        usernameEditLocator.first().click();
        usernameEditLocator.first().fill(employeeName.substring(0,1));

        page.waitForCondition(() -> {
            var visible = pimPageLocators.getListBoxItemByText(employeeName).isVisible();
            if (visible) {
                pimPageLocators.getListBoxItemByText(employeeName).click();
                log.info("==> Employee name entered: {}", employeeName);
            }
            return visible;
        });
    }

    public void clickOnSearchButton() {
        searchButtonLocator.click();
        page.waitForCondition(() -> {
            var visible = oneRecordFoundLocator.isVisible();
            if (visible) {
                log.info("==> Waited for Record Found");
            }
            return visible;
        });
    }

    public EmployeeData searchEmployeeByName(String employeeName) {
        enterEmployeeName(employeeName);
        clickOnSearchButton();

        var loc = pimPageLocators.getCellFirstMiddleNameLocator(employeeName);
        if (!loc.isVisible()) {
            return  null;
        }

        var cells = userRowLocator.allTextContents();

        return new EmployeeData(cells.get(1), cells.get(2), cells.get(3), cells.get(4));
    }
}


