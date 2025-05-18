package com.solutions.it.base;

import com.solutions.it.drivers.DriverFactory;
import com.solutions.it.utils.ConfigReader;
import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.util.Properties;

public class BaseTest {
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    protected Properties properties;
    
    @BeforeMethod
    public void setUp() {
        Log.info("Setting up test execution");
        properties = ConfigReader.loadProperties();
        String browser = properties.getProperty("browser");
        WebDriver driver = DriverFactory.createDriver(browser);
        
        // Configure timeouts
        long implicitWait = Long.parseLong(properties.getProperty("implicit.wait"));
        long pageLoadTimeout = Long.parseLong(properties.getProperty("page.load.timeout"));
        DriverFactory.configureTimeouts(driver, implicitWait, pageLoadTimeout);
        
        // Navigate to application URL
        String url = properties.getProperty("url");
        driver.get(url);
        Log.info("Navigated to: " + url);
        
        // Set the driver in ThreadLocal
        threadLocalDriver.set(driver);
    }
    
    @AfterMethod
    public void tearDown() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            Log.info("Closing the browser and ending test execution");
            driver.quit();
            threadLocalDriver.remove();
        }
    }
    
    public WebDriver getDriver() {
        return threadLocalDriver.get();
    }
} 