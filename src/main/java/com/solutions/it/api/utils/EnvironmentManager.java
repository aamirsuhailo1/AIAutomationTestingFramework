package com.solutions.it.api.utils;

import com.solutions.it.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * EnvironmentManager - Manages environment-specific configurations
 * Handles loading properties for different environments (qa, staging, demo)
 */
public class EnvironmentManager {
    private static final Logger LOGGER = LogManager.getLogger(EnvironmentManager.class);
    private static final String DEFAULT_ENV = "qa";
    private static final String ENV_PROPERTY_KEY = "env";
    private static Properties envProperties;
    
    private EnvironmentManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initializes the environment manager
     */
    public static void init() {
        String environment = System.getProperty(ENV_PROPERTY_KEY);
        if (environment == null || environment.isEmpty()) {
            environment = ConfigReader.getProperty(ENV_PROPERTY_KEY);
            if (environment == null || environment.isEmpty()) {
                environment = DEFAULT_ENV;
                LOGGER.warn("Environment not specified, using default: {}", DEFAULT_ENV);
            }
        }
        
        loadEnvironmentProperties(environment);
    }
    
    /**
     * Loads environment-specific properties
     * @param environment The environment name (qa, staging, demo)
     */
    private static void loadEnvironmentProperties(String environment) {
        String propertiesFilePath = "src/main/resources/" + environment + ".properties";
        envProperties = new Properties();
        
        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            envProperties.load(input);
            LOGGER.info("Loaded environment properties for: {}", environment);
        } catch (IOException e) {
            LOGGER.error("Failed to load environment properties for {}: {}", environment, e.getMessage());
            throw new RuntimeException("Failed to load environment properties: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets a property from the environment configuration
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        if (envProperties == null) {
            init();
        }
        
        String value = envProperties.getProperty(key);
        if (value == null) {
            LOGGER.warn("Property not found in environment configuration: {}", key);
        }
        
        return value;
    }
    
    /**
     * Gets the current environment name
     * @return Environment name (qa, staging, demo)
     */
    public static String getEnvironment() {
        if (envProperties == null) {
            init();
        }
        
        return envProperties.getProperty("environment.name", DEFAULT_ENV);
    }
    
    /**
     * Gets the base URL for API requests
     * @return Base URL for the current environment
     */
    public static String getBaseUrl() {
        return getProperty("api.baseUrl");
    }
    
    /**
     * Gets API endpoint for the specified service
     * @param service Service name
     * @return Endpoint URL
     */
    public static String getEndpoint(String service) {
        return getProperty("api.endpoint." + service);
    }
} 