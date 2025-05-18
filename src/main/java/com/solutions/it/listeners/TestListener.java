package com.solutions.it.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.solutions.it.base.BaseTest;
import com.solutions.it.reports.ExtentManager;
import com.solutions.it.utils.ConfigReader;
import com.solutions.it.utils.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class TestListener implements ITestListener {
    private final ExtentReports extent = ExtentManager.getInstance();
    
    @Override
    public void onStart(ITestContext context) {
        Log.info("================ Test Suite Started: " + context.getName() + " ================");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        Log.info("================ Test Suite Finished: " + context.getName() + " ================");
        extent.flush();
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        Log.startTestCase(result.getMethod().getMethodName());
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(), 
                                            result.getMethod().getDescription());
        ExtentManager.setTest(test);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        Log.info("Test Passed: " + result.getMethod().getMethodName());
        ExtentManager.getTest().log(Status.PASS, 
                MarkupHelper.createLabel(result.getMethod().getMethodName() + " - Test Passed", ExtentColor.GREEN));
        Log.endTestCase(result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        Log.error("Test Failed: " + result.getMethod().getMethodName());
        Log.error("Failure Reason: " + result.getThrowable().getMessage());
        
        // Log stack trace
        Log.error(Arrays.toString(result.getThrowable().getStackTrace()));
        
        // Capture screenshot
        captureScreenshot(result);
        
        // Log to Extent Reports
        ExtentTest test = ExtentManager.getTest();
        test.log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName() + " - Test Failed", ExtentColor.RED));
        test.log(Status.FAIL, result.getThrowable());
        
        Log.endTestCase(result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        Log.warn("Test Skipped: " + result.getMethod().getMethodName());
        ExtentTest test = ExtentManager.getTest();
        test.log(Status.SKIP, MarkupHelper.createLabel(result.getMethod().getMethodName() + " - Test Skipped", ExtentColor.ORANGE));
        test.log(Status.SKIP, result.getThrowable());
        Log.endTestCase(result.getMethod().getMethodName());
    }
    
    private void captureScreenshot(ITestResult result) {
        try {
            Object currentInstance = result.getInstance();
            WebDriver driver = null;
            
            if (currentInstance instanceof BaseTest) {
                driver = ((BaseTest) currentInstance).getDriver();
            }
            
            if (driver != null) {
                String screenshotPath = ConfigReader.getProperty("screenshot.path");
                if (screenshotPath == null || screenshotPath.isEmpty()) {
                    screenshotPath = "test-output/screenshots/";
                }
                
                // Ensure directory exists
                Path path = Paths.get(screenshotPath);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                
                String screenshotName = result.getMethod().getMethodName() + "_" + System.currentTimeMillis() + ".png";
                String fullPath = screenshotPath + screenshotName;
                
                // Take screenshot
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(screenshot.toPath(), Paths.get(fullPath));
                
                Log.info("Screenshot captured: " + fullPath);
                
                // Attach screenshot to extent report
                ExtentManager.getTest().addScreenCaptureFromPath(fullPath, "Failure Screenshot");
            } else {
                Log.warn("WebDriver instance was null. Unable to capture screenshot.");
            }
        } catch (IOException e) {
            Log.error("Failed to capture screenshot: " + e.getMessage(), e);
        }
    }
} 