package com.solutions.it.config;

import com.solutions.it.utils.ConfigReader;
import com.solutions.it.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Centralized configuration management for the framework.
 * Uses a singleton pattern with builder for flexible configuration.
 */
public class FrameworkConfig {
    private static FrameworkConfig instance;
    private final Properties properties;
    private final Map<String, Object> dynamicConfig;
    
    private FrameworkConfig() {
        this.properties = ConfigReader.loadProperties();
        this.dynamicConfig = new HashMap<>();
        Log.info("Framework configuration initialized");
    }
    
    public static synchronized FrameworkConfig getInstance() {
        if (instance == null) {
            instance = new FrameworkConfig();
        }
        return instance;
    }
    
    /**
     * Gets a configuration property
     * 
     * @param key The property key
     * @return The property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Gets a configuration property with default value
     * 
     * @param key The property key
     * @param defaultValue The default value if key not found
     * @return The property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets a boolean configuration property
     * 
     * @param key The property key
     * @return The boolean value
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
    
    /**
     * Gets an integer configuration property
     * 
     * @param key The property key
     * @return The integer value
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
    
    /**
     * Gets a long configuration property
     * 
     * @param key The property key
     * @return The long value
     */
    public long getLongProperty(String key) {
        return Long.parseLong(properties.getProperty(key));
    }
    
    /**
     * Sets a dynamic configuration value at runtime
     * 
     * @param key The configuration key
     * @param value The value to set
     */
    public void setDynamicProperty(String key, Object value) {
        dynamicConfig.put(key, value);
        Log.info("Dynamic config set: " + key + " = " + value);
    }
    
    /**
     * Gets a dynamic configuration value
     * 
     * @param key The configuration key
     * @return The configuration value
     */
    @SuppressWarnings("unchecked")
    public <T> T getDynamicProperty(String key) {
        return (T) dynamicConfig.get(key);
    }
    
    /**
     * Gets the browser name from configuration
     * 
     * @return The browser name
     */
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    /**
     * Gets the application URL from configuration
     * 
     * @return The application URL
     */
    public String getApplicationUrl() {
        return getProperty("url");
    }
    
    /**
     * Checks if headless mode is enabled
     * 
     * @return true if headless mode is enabled
     */
    public boolean isHeadless() {
        return getBooleanProperty("headless");
    }
    
    /**
     * Checks if remote execution is enabled (for Docker/Selenium Grid)
     * 
     * @return true if remote execution is enabled
     */
    public boolean isRemoteExecution() {
        // Check environment variable first (for Docker)
        String remoteEnv = System.getenv("SELENIUM_REMOTE");
        if (remoteEnv != null && !remoteEnv.isEmpty()) {
            return Boolean.parseBoolean(remoteEnv);
        }
        
        // Otherwise check config property
        return getBooleanProperty("remote.execution");
    }
    
    /**
     * Gets the Selenium Grid URL
     * 
     * @return the Grid URL
     */
    public String getGridUrl() {
        // Check environment variable first (for Docker)
        String gridUrlEnv = System.getenv("SELENIUM_GRID_URL");
        if (gridUrlEnv != null && !gridUrlEnv.isEmpty()) {
            return gridUrlEnv;
        }
        
        // Otherwise check config property
        return getProperty("grid.url", "http://localhost:4444/wd/hub");
    }
    
    /**
     * Gets the implicit wait timeout in seconds
     * 
     * @return The implicit wait timeout
     */
    public long getImplicitWaitTimeout() {
        return getLongProperty("implicit.wait");
    }
    
    /**
     * Gets the explicit wait timeout in seconds
     * 
     * @return The explicit wait timeout
     */
    public long getExplicitWaitTimeout() {
        return getLongProperty("explicit.wait");
    }
    
    /**
     * Gets the page load timeout in seconds
     * 
     * @return The page load timeout
     */
    public long getPageLoadTimeout() {
        return getLongProperty("page.load.timeout");
    }
    
    /**
     * Gets the screenshot path from configuration
     * 
     * @return The screenshot path
     */
    public String getScreenshotPath() {
        return getProperty("screenshot.path", "./test-output/screenshots/");
    }
    
    /**
     * Gets the default username from configuration
     * 
     * @return The default username
     */
    public String getDefaultUsername() {
        return getProperty("username");
    }
    
    /**
     * Gets the default password from configuration
     * 
     * @return The default password
     */
    public String getDefaultPassword() {
        return getProperty("password");
    }
    
    /**
     * Builder for dynamic configuration
     */
    public static class Builder {
        public Builder headless(boolean headless) {
            getInstance().setDynamicProperty("headless", headless);
            return this;
        }
        
        public Builder browser(String browser) {
            getInstance().setDynamicProperty("browser", browser);
            return this;
        }
        
        public Builder remoteExecution(boolean remote) {
            getInstance().setDynamicProperty("remote.execution", remote);
            return this;
        }
        
        public Builder gridUrl(String url) {
            getInstance().setDynamicProperty("grid.url", url);
            return this;
        }
        
        public Builder implicitWait(long seconds) {
            getInstance().setDynamicProperty("implicit.wait", seconds);
            return this;
        }
        
        public Builder explicitWait(long seconds) {
            getInstance().setDynamicProperty("explicit.wait", seconds);
            return this;
        }
        
        public Builder pageLoadTimeout(long seconds) {
            getInstance().setDynamicProperty("page.load.timeout", seconds);
            return this;
        }
        
        public FrameworkConfig build() {
            return getInstance();
        }
    }
} 