package com.solutions.it.base;

import com.solutions.it.config.FrameworkConfig;
import com.solutions.it.drivers.WebDriverManager;
import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.util.Map;

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
    public void setUp(@Optional String browser, Method method, Object[] testData, ITestContext context) {
        Log.info("Setting up test execution for method: " + method.getName());
        
        // First check if browser comes from a data provider parameter
        String browserToUse = browser;
        
        // If the test is using the crossBrowserData provider, extract browser from test data
        if (testData.length > 0 && testData[0] instanceof Map) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = (Map<String, Object>) testData[0];
                if (dataMap.containsKey("browser")) {
                    browserToUse = (String) dataMap.get("browser");
                    Log.info("Using browser from data provider: " + browserToUse);
                }
            } catch (ClassCastException e) {
                Log.warn("Could not extract browser from test data: " + e.getMessage());
            }
        }
        
        // Configure browser if provided via TestNG parameters or data provider
        if (browserToUse != null && !browserToUse.isEmpty()) {
            new FrameworkConfig.Builder().browser(browserToUse).build();
            Log.info("Using browser: " + browserToUse);
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