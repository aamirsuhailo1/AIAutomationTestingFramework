package com.solutions.it.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String PROPERTY_FILE_PATH = "qa.properties";
    private static Properties properties;
    
    private ConfigReader() {
        // Private constructor to prevent instantiation
    }
    
    public static Properties loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = new FileInputStream(PROPERTY_FILE_PATH)) {
                properties.load(input);
                Log.info("Properties file loaded successfully");
            } catch (IOException e) {
                Log.error("Error loading properties file: " + e.getMessage());
                throw new RuntimeException("Failed to load properties file: " + PROPERTY_FILE_PATH, e);
            }
        }
        return properties;
    }
    
    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }
} 