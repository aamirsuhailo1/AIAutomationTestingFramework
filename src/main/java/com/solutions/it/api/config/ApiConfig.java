package com.solutions.it.api.config;

import com.solutions.it.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * ApiConfig - Handles API configuration settings from properties files
 */
public class ApiConfig {
    private static final Logger LOGGER = LogManager.getLogger(ApiConfig.class);
    private static ApiConfig instance;
    
    private ApiConfig() {
        // Empty constructor
    }
    
    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }
    
    /**
     * Gets the base URL for API requests based on environment
     * @return Base URL as string
     */
    public String getBaseUrl() {
        String baseUrl = ConfigReader.getProperty("api.baseUrl");
        if (baseUrl == null || baseUrl.isEmpty()) {
            LOGGER.warn("API base URL not found in configuration. Using default.");
            return "https://jsonplaceholder.typicode.com"; // Default API for testing
        }
        return baseUrl;
    }
    
    /**
     * Gets the default headers for API requests
     * @return Map of header name-value pairs
     */
    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        
        // Add any custom headers from configuration
        String customHeaders = ConfigReader.getProperty("api.customHeaders");
        if (customHeaders != null && !customHeaders.isEmpty()) {
            String[] headerPairs = customHeaders.split(";");
            for (String pair : headerPairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    headers.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        
        return headers;
    }
    
    /**
     * Gets the authentication token if available
     * @return Authentication token or null
     */
    public String getAuthToken() {
        return ConfigReader.getProperty("api.authToken");
    }
    
    /**
     * Gets the timeout value for API requests
     * @return Timeout in seconds
     */
    public int getTimeoutInSeconds() {
        String timeout = ConfigReader.getProperty("api.timeout");
        if (timeout == null || timeout.isEmpty()) {
            return 30; // Default timeout
        }
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid timeout value in configuration. Using default.");
            return 30;
        }
    }
    
    /**
     * Gets environment-specific endpoint
     * @param endpointKey The key for the endpoint in properties file
     * @return The endpoint URL
     */
    public String getEndpoint(String endpointKey) {
        String endpoint = ConfigReader.getProperty("api.endpoint." + endpointKey);
        if (endpoint == null || endpoint.isEmpty()) {
            LOGGER.warn("Endpoint {} not found in configuration", endpointKey);
            return "";
        }
        return endpoint;
    }
} 