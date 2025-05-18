package com.solutions.it.base;

import com.solutions.it.config.FrameworkConfig;
import com.solutions.it.drivers.WebDriverManager;
import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base class for all test classes. 
 * Provides common setup and teardown operations.
 */
public class BaseTest {
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();
    
    /**
     * Sets up the test environment before each test method
     * 
     * @param browser Optional browser parameter that can be passed from TestNG
     */
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        Log.info("Setting up test execution");
        
        // Configure browser if provided via TestNG parameters
        if (browser != null && !browser.isEmpty()) {
            new FrameworkConfig.Builder().browser(browser).build();
            Log.info("Using browser from TestNG parameter: " + browser);
        }
        
        // Initialize WebDriver
        WebDriverManager.initializeDriver();
        
        // Navigate to application URL
        WebDriver driver = WebDriverManager.getDriver();
        String url = CONFIG.getApplicationUrl();
        driver.get(url);
        Log.info("Navigated to: " + url);
    }
    
    /**
     * Cleans up the test environment after each test method
     */
    @AfterMethod
    public void tearDown() {
        Log.info("Closing the browser and ending test execution");
        WebDriverManager.quitDriver();
    }
    
    /**
     * Gets the WebDriver instance for the current thread
     * 
     * @return the WebDriver instance
     */
    public WebDriver getDriver() {
        return WebDriverManager.getDriver();
    }
} 