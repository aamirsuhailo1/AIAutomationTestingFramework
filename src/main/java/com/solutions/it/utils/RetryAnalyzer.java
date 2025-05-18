package com.solutions.it.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer for handling flaky tests.
 * Will retry failed tests a specified number of times before reporting failure.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2; // Maximum number of retries
    
    /**
     * Determines if a failed test should be retried
     * 
     * @param result the test result
     * @return true if the test should be retried
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            Log.info("Retrying test: " + result.getName() + " for the " + (retryCount + 1) + " time");
            retryCount++;
            return true;
        }
        return false;
    }
    
    /**
     * Gets the current retry count
     * 
     * @return the current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Resets the retry count to 0
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
} 