package com.example.reporting;

import com.aventstack.extentreports.ExtentTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLogger {

    private final ExtentTest test;

    public TestLogger(ExtentTest test) {
        this.test = test;
    }

    public void step(String message) {
        test.info(message);
        log.info(message);
    }

    public void warn(String message) {
        test.warning(message);
        log.warn(message);
    }

    public void fail(String message, Throwable t) {
        test.fail(message);
        log.error(message, t);
    }
}