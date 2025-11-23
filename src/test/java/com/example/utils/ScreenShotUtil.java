package com.example.utils;

import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotUtil {

    public static String takeScreenShot(Page page, String testName) {
        var timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        var relativeDir = Paths.get("test-output", "screenshots");
        var fileName = testName + "_" + timeStamp + ".jpg";

        try {
            Files.createDirectories(relativeDir);
            Path screenshotPath = relativeDir.resolve(fileName);

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));
            return screenshotPath.toString();
        } catch (IOException e) {
            System.err.println("Failed to create screenshot directory or save screenshot: " + e.getMessage());
            return null;
        }
    }
}