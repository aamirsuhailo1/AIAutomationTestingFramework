package com.solutions.it.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger LOGGER = LogManager.getLogger(Log.class);
    
    private Log() {
        // Private constructor to prevent instantiation
    }
    
    public static void info(String message) {
        LOGGER.info(message);
    }
    
    public static void warn(String message) {
        LOGGER.warn(message);
    }
    
    public static void error(String message) {
        LOGGER.error(message);
    }
    
    public static void error(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }
    
    public static void debug(String message) {
        LOGGER.debug(message);
    }
    
    public static void startTestCase(String testCaseName) {
        LOGGER.info("======================================================");
        LOGGER.info("Starting Test Case: " + testCaseName);
        LOGGER.info("======================================================");
    }
    
    public static void endTestCase(String testCaseName) {
        LOGGER.info("======================================================");
        LOGGER.info("End of Test Case: " + testCaseName);
        LOGGER.info("======================================================");
    }
} 