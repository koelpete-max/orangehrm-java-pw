package com.example.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.example.base.BaseTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Paths;

public class ExtentTestNgListener implements ITestListener  {

    @Override
    public void onTestStart(ITestResult result) {
        // Optional: hier KÖNNTEST du ReportManager.startTest(...) hin verschieben
        // Aktuell machst du das in BaseTest.beforeAnyTest – das ist völlig okay.
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ReportManager.getTest();
        if (test != null) {
            test.pass("Test PASSED");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ReportManager.getTest();
        if (test != null) {
            Object instance = result.getInstance();
            if (instance instanceof BaseTest base) {
                String path = base.captureScreenshotForListener(result.getName());
                if (path != null) {
                    String fileName = Paths.get(path).getFileName().toString();
                    String relativeToReport = "screenshots/" + fileName;
                    test.addScreenCaptureFromPath(relativeToReport, "Failure screenshot");
                }
            }
            test.fail(result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ReportManager.getTest();
        if (test != null) {
            if (result.getThrowable() != null) {
                test.skip(result.getThrowable());
            } else {
                test.skip("Test SKIPPED");
            }
        }
    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}