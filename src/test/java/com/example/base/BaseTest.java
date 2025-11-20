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
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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

    @BeforeMethod
    public  void setUp(Method method) throws IOException {

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--start-maximized"))
                        .setHeadless(false)
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
