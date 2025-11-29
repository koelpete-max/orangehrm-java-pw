package com.example.setup;

import com.example.utils.ScreenShotUtil;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrangeHrmSetupWizard {

    private static String screenshotPath;

    private static final String TEST_NAME = "BeforeSuite";

    private OrangeHrmSetupWizard() {}

    public static String runIfNeededReturnScreenshot(Page page) {

        if (!page.locator("//img[@alt='orangehrm-branding']").isVisible()) {
            return ScreenShotUtil.takeScreenShot(page, TEST_NAME);
        }

        log.info("Setup Wizard is there!!");

        try {
            var upgradeLocator = getUpgradeLocator(page);
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            checkOrangehrmStarterPage(page, upgradeLocator);
            checkEncryptionPage(page);
            checkSystemCheckPage(page);
            checkCurrentVersionDetailsPage(page);
            checkUpgradingOrangePage(page);
            checkUpgradeCompletePage(page);

            log.info("OrangeHRM page is ready!!");
        }
        finally {
            return screenshotPath;
        }

    }

    private static Locator getUpgradeLocator(Page page) {

        var upgradeLocator = page.locator("label")
                .filter(new Locator.FilterOptions()
                        .setHasText("Upgrading an Existing"))
                .locator("span");
        if (!upgradeLocator.isVisible()) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }
        return upgradeLocator;
    }

    private static void checkOrangehrmStarterPage(Page page, Locator upgradeLocator) {

        log.info("==> Welcome to OrangeHRM Starter");
        upgradeLocator.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Next")).click();
        page.locator("i.oxd-icon.bi-check.oxd-checkbox-input-icon")
                .click();  // NEED Continue Button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Continue")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForCondition(() -> {
            return page.getByRole(AriaRole.TEXTBOX).first().isVisible();
        });
        if (!page.getByRole(AriaRole.HEADING).textContent().contains("Database Information".trim())) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }
    }

    private static void checkEncryptionPage(Page page) {

        log.info("==> ENCRYPTION");
        page.getByRole(AriaRole.TEXTBOX).first().fill("orangehrm-db");
        page.getByRole(AriaRole.TEXTBOX).nth(2).fill("orangehrm");
        page.getByRole(AriaRole.TEXTBOX).nth(3).fill("orangeuser");
        page.locator("input[type=\"password\"]").fill("orangepass");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForCondition(() -> {
            var success = page.getByRole(AriaRole.HEADING).textContent().contains("System Check".trim());
            if (success) {
                log.info("==> System Check heading is ready");
            }
            return success;
        });
        if (!page.getByRole(AriaRole.HEADING).textContent().contains("System Check".trim())) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }
    }

    private static void checkSystemCheckPage(Page page) {

        log.info("==> System Check");
        waitForPageCondition(page, "Next", "==> Button next page System is ready");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        if (!page.getByRole(AriaRole.HEADING).textContent().contains("Current Version Details")) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }
    }

    private static void checkCurrentVersionDetailsPage(Page page) {

        log.info("==> Current Version Details");
        waitForPageCondition(page, "5.7", "==> Current OrangeHRM Version selection is ready");
        if (!page.getByText("5.7", new Page.GetByTextOptions().setExact(true)).isVisible()) {
            throw new IllegalStateException("Current Version Details page failed - Version 5.7 not found");
        }
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();

        waitForPageCondition(page, "Upgrading OrangeHRM", "==> Upgrading OrangeHRM is ready");
        if (!page.getByText("Upgrading OrangeHRM").isVisible()) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }

    }

    private static void checkUpgradingOrangePage(Page page) {

        final var TEST_NAME = "Upgrading OrangeHRM page failed";

        log.info("==> Upgrading OrangeHRM");
        waitForPageCondition(page, "Next", "==> 100% bar ready");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        if (!page.getByRole(AriaRole.HEADING).textContent().contains("Upgrade Complete")) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }
    }

    private static void checkUpgradeCompletePage(Page page) {

        final var TEST_NAME = "Upgrade Complete page failed";

        log.info("==> Upgrade Complete");
        waitForPageCondition(page, "Launch OrangeHRM", "==> Launch OrangeHRM button is ready");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                        .setName("Launch OrangeHRM"))
                .click();
        page.waitForCondition(() -> {
            return page.locator("//img[@alt='company-branding']").isVisible();
        });
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        if (!page.locator("//img[@alt='company-branding']").isVisible()) {
            screenshotPath = ScreenShotUtil.takeScreenShot(page, TEST_NAME);
            throw new IllegalStateException(TEST_NAME);
        }
    }

    private static void waitForPageCondition(Page page, String text, String message) {
        page.waitForCondition(() -> {
            var visible = page.getByText(text, new Page.GetByTextOptions().setExact(true)).isVisible();
            if (visible) {
                log.info(message);
            }
            return visible;
        });
    }
}