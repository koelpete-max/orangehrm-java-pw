package com.example.pages.pim;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class PimPage {
    final Page page;

    final String employeeNameFieldText = "Type for hints...";
    final String searButtonXpath = "[type='submit']";
    final String cellFirstMiddleNameXpath = "//div[contains(text(),'FirstMiddleName')]";

    @Inject
    public PimPage(Page page) {
        this.page = page;
    }

    public void enterEmployeeName(String employeeName) {
        page.getByRole(AriaRole.TEXTBOX,
                        new Page.GetByRoleOptions().setName(employeeNameFieldText)).first().click();
        page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName(employeeNameFieldText)).first().fill("B");

        page.waitForCondition(() -> {
            var visible = page.getByRole(AriaRole.LISTBOX).getByText(employeeName).isVisible();
            if (visible) {
                page.getByRole(AriaRole.LISTBOX).getByText(employeeName).click();
                log.info("Employee name entered: {}", employeeName);
            }
            return visible;
        });
    }

    public void clickOnSearchButton() {
        page.locator(searButtonXpath).click();
    }

    public EmployeeData searchEmployeeByName(String employeeFirstName) {
        enterEmployeeName(employeeFirstName);
        clickOnSearchButton();

        var loc = cellFirstMiddleNameXpath.replace("FirstMiddleName", employeeFirstName);
        if (!page.locator(loc).isVisible()) {
            return  null;
        }

        var cells = page.locator("//*[@class='oxd-table-cell oxd-padding-cell']").allTextContents();

        var employeeData = new EmployeeData();
        employeeData.setId(cells.get(1));
        employeeData.setFirstMiddleName(cells.get(2));
        employeeData.setLastName(cells.get(3));
        employeeData.setJobTitle(cells.get(4));
        return employeeData;
    }
}


