package com.solutions.it.drivers;

import com.solutions.it.config.FrameworkConfig;
import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages WebDriver instances and provides a factory for creating different browser drivers.
 */
public class WebDriverManager {
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    private static final Map<String, WebDriverFactory> FACTORIES = new HashMap<>();
    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();
    
    // Register all available browser factories
    static {
        FACTORIES.put("chrome", new ChromeDriverFactory());
        FACTORIES.put("firefox", new FirefoxDriverFactory());
        FACTORIES.put("edge", new EdgeDriverFactory());
        FACTORIES.put("safari", new SafariDriverFactory());
    }
    
    private WebDriverManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the current thread's WebDriver instance or creates a new one if none exists
     * 
     * @return The WebDriver instance
     */
    public static WebDriver getDriver() {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            initializeDriver();
        }
        return DRIVER_THREAD_LOCAL.get();
    }
    
    /**
     * Initializes a new WebDriver instance based on configured browser
     */
    public static void initializeDriver() {
        String browser = CONFIG.getBrowser().toLowerCase();
        
        WebDriverFactory factory = FACTORIES.getOrDefault(browser, FACTORIES.get("chrome"));
        WebDriver driver = factory.createDriver();
        
        configureTimeouts(driver);
        DRIVER_THREAD_LOCAL.set(driver);
        
        Log.info("WebDriver initialized for browser: " + browser);
    }
    
    /**
     * Configures timeouts for the WebDriver instance
     * 
     * @param driver The WebDriver instance
     */
    private static void configureTimeouts(WebDriver driver) {
        long implicitWait = CONFIG.getImplicitWaitTimeout();
        long pageLoadTimeout = CONFIG.getPageLoadTimeout();
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        
        Log.info("Configured timeouts - Implicit Wait: " + implicitWait + "s, Page Load: " + pageLoadTimeout + "s");
    }
    
    /**
     * Quits the WebDriver instance and removes it from the ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            Log.info("Quitting WebDriver instance");
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
    
    /**
     * Interface for WebDriver factory implementations
     */
    private interface WebDriverFactory {
        WebDriver createDriver();
    }
    
    /**
     * Factory for Chrome WebDriver
     */
    private static class ChromeDriverFactory implements WebDriverFactory {
        @Override
        public WebDriver createDriver() {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            boolean headless = CONFIG.isHeadless();
            
            if (headless) {
                options.addArguments("--headless");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
            }
            
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.setAcceptInsecureCerts(true);
            
            Log.info("Creating Chrome WebDriver with headless=" + headless);
            return new ChromeDriver(options);
        }
    }
    
    /**
     * Factory for Firefox WebDriver
     */
    private static class FirefoxDriverFactory implements WebDriverFactory {
        @Override
        public WebDriver createDriver() {
            io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            boolean headless = CONFIG.isHeadless();
            
            if (headless) {
                options.addArguments("--headless");
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");
            }
            
            options.setAcceptInsecureCerts(true);
            
            Log.info("Creating Firefox WebDriver with headless=" + headless);
            return new FirefoxDriver(options);
        }
    }
    
    /**
     * Factory for Edge WebDriver
     */
    private static class EdgeDriverFactory implements WebDriverFactory {
        @Override
        public WebDriver createDriver() {
            io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            boolean headless = CONFIG.isHeadless();
            
            if (headless) {
                options.addArguments("--headless");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
            }
            
            options.setAcceptInsecureCerts(true);
            
            Log.info("Creating Edge WebDriver with headless=" + headless);
            return new EdgeDriver(options);
        }
    }
    
    /**
     * Factory for Safari WebDriver
     */
    private static class SafariDriverFactory implements WebDriverFactory {
        @Override
        public WebDriver createDriver() {
            SafariOptions options = new SafariOptions();
            Log.info("Creating Safari WebDriver");
            return new SafariDriver(options);
        }
    }
} 