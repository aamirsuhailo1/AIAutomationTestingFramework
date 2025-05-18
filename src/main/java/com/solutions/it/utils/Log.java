package com.solutions.it.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger LOGGER = LogManager.getLogger(Log.class);
    
    private Log() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get the logger instance
     * 
     * @return the logger instance
     */
    public static Logger getLogger() {
        return LOGGER;
    }
    
    public static void info(String message) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(message);
        }
    }
    
    public static void warn(String message) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(message);
        }
    }
    
    public static void error(String message) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message);
        }
    }
    
    public static void error(String message, Throwable throwable) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message, throwable);
        }
    }
    
    public static void debug(String message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }
    }
    
    public static void startTestCase(String testCaseName) {
        if (LOGGER.isInfoEnabled()) {
            final String separator = "======================================================";
            LOGGER.info(separator);
            LOGGER.info("Starting Test Case: " + testCaseName);
            LOGGER.info(separator);
        }
    }
    
    public static void endTestCase(String testCaseName) {
        if (LOGGER.isInfoEnabled()) {
            final String separator = "======================================================";
            LOGGER.info(separator);
            LOGGER.info("End of Test Case: " + testCaseName);
            LOGGER.info(separator);
        }
    }
} 