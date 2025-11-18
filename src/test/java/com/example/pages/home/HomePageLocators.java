package com.example.pages.home;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePageLocators {
    private final Page page;

    public HomePageLocators(Page page) {
        this.page = page;
    }

    public String getPageTitle() {
        return page.title();
    }

    public Locator getCompanyBrandImgLocator() {
        final String companyBrandImg = "//img[@alt='company-branding']";
        return page.locator(companyBrandImg);
    }
}
