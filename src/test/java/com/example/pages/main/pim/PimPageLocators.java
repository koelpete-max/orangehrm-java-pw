package com.example.pages.main.pim;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class PimPageLocators {

    final Page page;

    final String employeeNameFieldText = "Type for hints...";
    final String searButtonXpath = "[type='submit']";
    final String oneRecordFoundText = "(1) Record Found";
    final String cellFirstMiddleNameXpath = "//div[contains(text(),'FirstMiddleName')]";
    final String rowXpath = "//*[@class='oxd-table-cell oxd-padding-cell']";

    public PimPageLocators(Page page) {
        this.page = page;
    }

    public Locator getUserameEditLocator() {
        return page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName(employeeNameFieldText));
    }

    public Locator getListBoxItemByText(String searchText) {
        return page.getByRole(AriaRole.LISTBOX).getByText(searchText);
    }

    public Locator getSearchButtonLocator() {
        return page.locator(searButtonXpath);
    }

    public Locator getOneRecordFoundTextLocator() {
        return page.getByText(oneRecordFoundText);
    }

    public Locator getCellFirstMiddleNameLocator(String firstMiddleName) {
        var xpath = cellFirstMiddleNameXpath.replace("FirstMiddleName", firstMiddleName);
        return page.locator(xpath);
    }

    public Locator getUserRowLocator() {
        return page.locator(rowXpath);
    }
}
