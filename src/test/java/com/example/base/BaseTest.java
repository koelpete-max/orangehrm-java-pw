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
import com.example.utils.EnvConfig;
import com.example.utils.TestUserProvider;
import com.example.setup.OrangeHrmSetupWizard;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class BaseTest {

    protected TestComponent di;

    protected ExtentReports extent;
    protected ExtentTest test;
    protected TestLogger testLog;

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
    public void beforeSuite() {
        log.info("Setting up Test Suite");

        baseUrl = EnvConfig.resolveBaseUrl();
        extent = ExtentManager.getInstance();

        try (Playwright pw = Playwright.create()) {
            Browser tmpBrowser = pw.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList("--start-maximized"))
                            .setHeadless(false)
            );
            Page tmpPage = tmpBrowser.newPage();

            ExtentTest suiteTest = extent.createTest("suiteSetup");
            TestLogger suiteLog = new TestLogger(suiteTest);

            suiteLog.step("Navigate to Base URL: " + baseUrl);
            tmpPage.navigate(baseUrl);
            tmpPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
            tmpPage.waitForLoadState(LoadState.NETWORKIDLE);

            OrangeHrmSetupWizard.runIfNeeded(tmpPage);

            takeScreenshot(tmpPage, suiteTest, "suiteSetup");
            suiteLog.step("Closing Browser");
            tmpBrowser.close();
            extent.flush();
        }
    }

    @BeforeMethod
    public void setUpTest(Method method) throws IOException {

        log.info("Starting Test: {}", method.getName());

        baseUrl = EnvConfig.resolveBaseUrl();

        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--start-maximized"))
                        .setHeadless(headless)
        );
        page = browser.newPage();

        // DI / Pages
        di = DaggerTestComponent.factory().create(page);
        homePage = di.homePage();
        loginPage = di.loginPage();
        adminPage = di.adminPage();
        pimPage = di.pimPage();
        dashboardPage = di.dashboardPage();
        sidePanel = di.sidePanel();
        topbarPanel = di.topbarPanel();

        // Config & User
        defaultTestUser = TestUserProvider.getDefaultAdmin();

        // Reporting
        extent = ExtentManager.getInstance();
        test = extent.createTest(method.getName());
        testLog = new TestLogger(test);

        log.info("Test '{}' started", method.getName());
    }

    @AfterMethod
    public void tearDownTest(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot(page, test, result.getName());
                test.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test PASSED");
            } else {
                test.skip("Test SKIPPED");
            }
            extent.flush();
        } finally {
            if (browser != null) {
                log.info("Closing Browser");
                browser.close();
            }
            if (playwright != null) {
                log.info("Closing Playwright");
                playwright.close();
            }
        }
    }

    // =========================
    //   Helpers
    // =========================
    private void takeScreenshot(Page page, ExtentTest test, String screenshotText) {
        String screenshotPath = ScreenShotUtil.takeScreenShot(page, screenshotText);
        log.info("Screenshot stored at: {}", screenshotPath);

        String fileName = Paths.get(screenshotPath).getFileName().toString();
        String relativeToReport = "screenshots/" + fileName;

        test.addScreenCaptureFromPath(relativeToReport, "screenshot");
    }

    protected void navigateToHomePage(String url) {
        loginPage = homePage.navigateTo(url);
    }

}