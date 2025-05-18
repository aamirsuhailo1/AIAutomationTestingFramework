package com.solutions.it.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String PROPERTY_FILE_PATH = "src/main/resources/config.properties";
    private static volatile Properties properties;
    
    private ConfigReader() {
        // Private constructor to prevent instantiation
    }
    
    public static Properties loadProperties() {
        Properties result = properties;
        if (result == null) {
            synchronized (ConfigReader.class) {
                result = properties;
                if (result == null) {
                    result = new Properties();
                    try (InputStream input = new FileInputStream(PROPERTY_FILE_PATH)) {
                        result.load(input);
                        if (Log.getLogger().isInfoEnabled()) {
                            Log.info("Properties file loaded successfully");
                        }
                        properties = result;
                    } catch (IOException e) {
                        if (Log.getLogger().isErrorEnabled()) {
                            Log.error("Error loading properties file: " + e.getMessage());
                        }
                        throw new RuntimeException("Failed to load properties file: " + PROPERTY_FILE_PATH, e);
                    }
                }
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