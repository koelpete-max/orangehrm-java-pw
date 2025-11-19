package com.example.utils;

import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

// Screenshot-Util
public class ScreenShotUtil {

    public static String takeScreenShot(Page page, String name) {
        var timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = name.replaceAll("[^a-zA-Z0-9-_]", "_") + timeStamp + ".jpg";
        Path target = Paths.get("test-results", "screenshots", fileName);
        try {
            Files.createDirectories(target.getParent());
            page.screenshot(new Page.ScreenshotOptions().setPath(target));
            return target.toString(); // z.B. "test-results/screenshots/LoginTest.png"
        } catch (IOException e) {
            throw new RuntimeException("Could not create screenshot", e);
        }
    }
}
//public class ScreenShotUtil {
//
//    public static String takeScreenShot(Page page, String testName) {
//
//        var timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        var screenshotPath = "test-output/screenshots/" +testName + "_" + timeStamp + ".jpg";
//
//        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)).setFullPage(true));
//        return screenshotPath;
//    }
//}
