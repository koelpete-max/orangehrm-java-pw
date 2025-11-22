package com.example.base;


import com.example.di.DaggerTestComponent;
import com.example.di.TestComponent;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.example.pages.main.dashboard.DashboardPage;
import com.example.pages.home.HomePage;
import com.example.pages.login.LoginPage;
import com.example.pages.main.SidePanel;
import com.example.pages.main.TopbarPanel;
import com.example.pages.main.admin.AdminPage;
import com.example.pages.main.pim.PimPage;
import com.example.reporting.TestLogger;
import com.example.utils.ExtentManager;
import com.example.utils.ScreenShotUtil;
import com.example.utils.TestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class BaseTest {

    protected TestComponent di;

    protected ExtentReports extent;
    protected ExtentTest test;
    protected TestLogger tlog;

    protected HomePage homePage;
    protected LoginPage loginPage;
    protected AdminPage adminPage;
    protected PimPage pimPage;
    protected DashboardPage dashboardPage;
    protected SidePanel sidePanel;
    protected TopbarPanel topbarPanel;

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    protected String baseUrl;

    protected TestUser defaultTestUser;

    @BeforeSuite
    public void setupSuite() {
        log.info("Setting up Test Suite");
        var playwright = Playwright.create();
        var browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--start-maximized"))
                        .setHeadless(true)
        );
        var page = browser.newPage();

        baseUrl = System.getenv("BASE_URL");
        baseUrl = (baseUrl == null || baseUrl.isBlank()) ? TestConfig.get("BASE_URL") : baseUrl;
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("BASE_URL is not set");
        }

        page.navigate(baseUrl);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        if (page.locator("//img[@alt='orangehrm-branding']").isVisible()) {
            log.info("Setup Wizard is there!!");
            goThroughSetupWizard(page);
        }
        browser.close();
    }

    @BeforeMethod
    public  void setUp(Method method) throws IOException {

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--start-maximized"))
                        .setHeadless(true)
        );
        page = browser.newPage();

        di = DaggerTestComponent.factory().create(page);

        homePage = di.homePage();
        loginPage = di.loginPage();
        adminPage = di.adminPage();
        pimPage = di.pimPage();
        dashboardPage = di.dashboardPage();
        sidePanel = di.sidePanel();
        topbarPanel = di.topbarPanel();

        extent = ExtentManager.getInstance();
        test = extent.createTest(method.getName());
        tlog = new TestLogger(test);

        baseUrl = System.getenv("BASE_URL");
        baseUrl = (baseUrl == null || baseUrl.isBlank()) ? TestConfig.get("BASE_URL") : baseUrl;
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("BASE_URL is not set");
        }

        setDefaultTestUser();

        log.info("Test '{}' started ", method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result);
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            takeScreenshot(result);
            test.pass("Test PASSED");
        } else {
            test.skip("Test SKIPPED");
        }
        extent.flush();

        if (browser != null) {
            log.info("Closing Browser");
            browser.close();
        }

        if (playwright != null) {
            log.info("Closing Playwright");
            playwright.close();
        }
    }

    private void takeScreenshot(ITestResult result) {
        String screenshotPath = ScreenShotUtil.takeScreenShot(page, result.getName());
        log.info("Screenshot stored at: {}", screenshotPath);

        String fileName = Paths.get(screenshotPath).getFileName().toString();
        String relativeToReport = "screenshots/" + fileName;

        test.addScreenCaptureFromPath(relativeToReport, "screenshot");
    }

    protected void navigateToHomePage(String url) {
        loginPage = homePage.navigateTo(url);
    }

    private void goThroughSetupWizard(Page page) {

        // Welcome to OrangeHRM Starter
        var upgradeLocator = page.locator("label")
                .filter(new Locator.FilterOptions()
                        .setHasText("Upgrading an Existing"))
                .locator("span");
        if (!upgradeLocator.isVisible()) {
            return;
        }

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
            throw new IllegalStateException("Welcome to OrangeHRM Starter page failed");
        }

        // ENCRYPTION
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
            throw new IllegalStateException("ENCRYPTION page failed");
        }

        // System Check
        log.info("==> System Check");
        waitForPageCondition(page, "Next", "==> Button next page System is ready");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        if (!page.getByRole(AriaRole.HEADING).textContent().contains("Current Version Details")) {
            throw new IllegalStateException("System Check page failed");
        }

        // Current Version Details
        log.info("==> Current Version Details");
        waitForPageCondition(page, "5.7", "==> Current OrangeHRM Version selection is ready");
        if (!page.getByText("5.7", new Page.GetByTextOptions().setExact(true)).isVisible()) {
            throw new IllegalStateException("Current Version Details page failed - Version 5.7 not found");
        }
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();

        waitForPageCondition(page, "Upgrading OrangeHRM", "==> Upgrading OrangeHRM is ready");
        if (!page.getByText("Upgrading OrangeHRM").isVisible()) {
            throw new IllegalStateException("Current Version Details page failed");
        }

        // Upgrading OrangeHRM
        log.info("==> Upgrading OrangeHRM");
        waitForPageCondition(page, "Next", "==> 100% bar ready");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        if (!page.getByRole(AriaRole.HEADING).textContent().contains("Upgrade Complete")) {
            throw new IllegalStateException("Upgrading OrangeHRM page failed");
        }

        // Upgrade Complete
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
            throw new IllegalStateException("Upgrade Complete page failed");
        }

        log.info("OrangeHRM page is ready!!");
    }

    private void waitForPageCondition(Page page, String text, String message) {
        page.waitForCondition(() -> {
            var visible = page.getByText(text, new Page.GetByTextOptions().setExact(true)).isVisible();
            if (visible) {
                log.info(message);
            }
            return visible;
        });
    }

    private void setDefaultTestUser() throws NullPointerException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("config/test-users.json");
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(is);

        var adminNode = root.get("Admin");
        defaultTestUser = new TestUser(adminNode.get("username").asText(),
                adminNode.get("password").asText()
        );
        log.info("Default TestUser: '{}'", defaultTestUser.username);
    }

    protected record TestUser(String username, String password) {

    }
}
