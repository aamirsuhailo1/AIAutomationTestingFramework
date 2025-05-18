package com.solutions.it.drivers;

import com.solutions.it.utils.ConfigReader;
import com.solutions.it.utils.Log;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.Properties;

public class DriverFactory {
    
    public static WebDriver createDriver(String browser) {
        WebDriver driver;
        Properties properties = ConfigReader.loadProperties();
        boolean headless = Boolean.parseBoolean(properties.getProperty("headless"));
        
        Log.info("Creating driver for browser: " + browser);
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
                
            case "safari":
                driver = new SafariDriver();
                break;
                
            default:
                Log.warn("Invalid browser specified, defaulting to Chrome");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
        
        if (!browser.equalsIgnoreCase("safari")) {
            driver.manage().window().maximize();
        }
        
        return driver;
    }
    
    public static void configureTimeouts(WebDriver driver, long implicitWaitSeconds, long pageLoadTimeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeoutSeconds));
        Log.info("Configured timeouts - Implicit Wait: " + implicitWaitSeconds + "s, Page Load: " + pageLoadTimeoutSeconds + "s");
    }
} 