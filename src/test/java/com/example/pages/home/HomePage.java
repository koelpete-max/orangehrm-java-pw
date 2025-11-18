package com.example.pages.home;

import com.example.pages.login.LoginPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class HomePage {

    private final Page page;
    private final String expectedPageTitle = "OrangeHRM";
    private final HomePageLocators homePageLocators;
    private final Locator CompanyBrandImgLocator;

    @Inject
    public HomePage(Page page) {
        this.page = page;
        homePageLocators = new HomePageLocators(page);
        CompanyBrandImgLocator = homePageLocators.getCompanyBrandImgLocator();
    }

    public LoginPage navigateTo(String url) {
        log.info("Navigating to '{}'", url);
        page.navigate(url,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );
        page.waitForLoadState(LoadState.NETWORKIDLE);

        var success = homePageLocators.getPageTitle().equals(expectedPageTitle)
                && CompanyBrandImgLocator.isVisible();
        return success ? new LoginPage(page) : null;
    }

    public boolean isPageReady() {
        var isReady = homePageLocators.getPageTitle().equals(expectedPageTitle)
                && CompanyBrandImgLocator.isVisible();
        log.info("Page ready '{}'", isReady ? "Yes" : "No");
        return isReady;
    }
}
