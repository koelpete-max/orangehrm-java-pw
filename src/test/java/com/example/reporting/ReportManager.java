// com/example/reporting/ReportManager.java
package com.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.example.utils.ExtentManager;

public class ReportManager {

    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    public static void startTest(String testName) {
        ExtentReports extent = ExtentManager.getInstance();
        ExtentTest test = extent.createTest(testName);
        CURRENT_TEST.set(test);
    }

    public static ExtentTest getTest() {
        return CURRENT_TEST.get();
    }

    public static void flush() {
        ExtentReports extent = ExtentManager.getInstance();
        extent.flush();
        CURRENT_TEST.remove();
    }
}