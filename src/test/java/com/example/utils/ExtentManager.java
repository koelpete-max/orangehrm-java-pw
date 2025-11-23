package com.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            try {
                Path reportDir = Paths.get("test-output");
                Files.createDirectories(reportDir);   // ✅ ensure folder exists

                String reportPath = reportDir.resolve("ExtentReport.html").toString();
                ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

                extent = new ExtentReports();
                extent.attachReporter(reporter);
            } catch (IOException e) {
                // fallback: at least have something, and log clearly
                System.err.println("Failed to create test-output directory: " + e.getMessage());
                ExtentSparkReporter reporter = new ExtentSparkReporter("ExtentReport.html");
                extent = new ExtentReports();
                extent.attachReporter(reporter);
            }
        }
        return extent;
    }
}
