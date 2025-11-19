package com.example.utils;

import com.microsoft.playwright.Page;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotUtil {

    public static String takeScreenShot(Page page, String testName) {

        var timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        var screenshotPath = "test-output/screenshots/" +testName + "_" + timeStamp + ".jpg";

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)).setFullPage(true));
        return screenshotPath;
    }
}
