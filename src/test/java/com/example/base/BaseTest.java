package com.example.base;

import com.example.core.TestContext;
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
import com.example.reporting.ReportManager;
import com.example.reporting.TestLogger;
import com.example.utils.ExtentManager;
import com.example.utils.ScreenShotUtil;
import com.example.utils.EnvConfig;
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
    protected TestContext testContext;

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

    protected static final String BASE_URL = EnvConfig.resolveBaseUrl();
    protected TestUser defaultTestUser;

    @BeforeSuite
    public void setUpSuite() {
        log.info("Setting up Test Suite");

        extent = ExtentManager.getInstance();

        boolean headless = Boolean.parseBoolean(
                System.getenv().getOrDefault(
                        "HEADLESS",
                        "false"
                )
        );
        try (Playwright pw = Playwright.create()) {
            Browser tmpBrowser = pw.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList("--start-maximized"))
                            .setHeadless(headless)
            );
            Page tmpPage = tmpBrowser.newPage();

            ExtentTest suiteTest = extent.createTest("suiteSetup");
            TestLogger suiteLog = new TestLogger(suiteTest);

            suiteLog.step("Navigate to Base URL: " + BASE_URL);
            tmpPage.navigate(BASE_URL);
            tmpPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
            tmpPage.waitForLoadState(LoadState.NETWORKIDLE);

            var screenshotPath = OrangeHrmSetupWizard.runIfNeededReturnScreenshot(tmpPage);

            takeScreenshot(tmpPage, screenshotPath, suiteTest, "suiteSetup");
            suiteLog.step("Closing Browser");
            tmpBrowser.close();
            extent.flush();
        }
    }

    @BeforeMethod
    public void setUpTest(Method method) throws IOException {

        di = DaggerTestComponent.factory().create();

        testContext = di.testContext();

        this.playwright = testContext.getPlaywright();
        this.browser = testContext.getBrowser();
        this.page = testContext.getPage();
        this.defaultTestUser = testContext.getDefaultTestUser();

        // Pages
        homePage = di.homePage();
        loginPage = di.loginPage();
        adminPage = di.adminPage();
        pimPage = di.pimPage();
        dashboardPage = di.dashboardPage();
        sidePanel = di.sidePanel();
        topbarPanel = di.topbarPanel();

        // Reporting
        ReportManager.startTest(method.getName());
        test = ReportManager.getTest();
        testLog = new TestLogger(test);

        log.info("Test '{}' started", method.getName());
    }

    @AfterMethod
    public void afterAnyTest(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot(page, test, result.getName());
                test.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test PASSED");
            } else {
                test.skip("Test SKIPPED");
            }
            ReportManager.flush();
        } finally {
            if (testContext != null) {
                log.info("Closing TestContext resources");
                testContext.close();
            }
        }
    }

    // =========================
    //   Helpers
    // =========================
    private void takeScreenshot(Page page, ExtentTest test, String screenshotText) {
        String screenshotPath = ScreenShotUtil.takeScreenShot(page, screenshotText);
        takeScreenshot(page, screenshotPath, test, screenshotText);
    }

    private void takeScreenshot(Page page,
                                String screenshotPath,
                                ExtentTest test,
                                String screenshotText) {
        log.info("Screenshot stored at: {}", screenshotPath);

        String fileName = Paths.get(screenshotPath).getFileName().toString();
        String relativeToReport = "screenshots/" + fileName;

        test.addScreenCaptureFromPath(relativeToReport, screenshotText);
    }


    protected void navigateToHomePage(String url) {
        loginPage = homePage.navigateTo(url);
    }

}