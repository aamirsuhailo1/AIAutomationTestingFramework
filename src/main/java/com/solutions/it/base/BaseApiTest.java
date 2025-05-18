package com.solutions.it.base;

import com.aventstack.extentreports.ExtentTest;
import com.solutions.it.api.RestAssuredClient;
import com.solutions.it.api.config.ApiConfig;
import com.solutions.it.api.utils.EnvironmentManager;
import com.solutions.it.api.validators.ResponseValidator;
import com.solutions.it.reports.ExtentManager;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseApiTest - Base class for all API tests
 * Provides common setup and utility methods for API testing
 * Completely separate from UI tests - does not initialize WebDriver
 */
public class BaseApiTest {
    protected static final Logger LOGGER = LogManager.getLogger(BaseApiTest.class);
    protected RestAssuredClient apiClient;
    protected ApiConfig apiConfig;
    protected ExtentTest extentTest;
    protected static final int DEFAULT_TIMEOUT_MS = 5000;
    
    @BeforeSuite
    public void beforeSuite() {
        // Initialize environment
        EnvironmentManager.init();
        
        // Set system property to skip WebDriver initialization
        System.setProperty("skipBrowser", "true");
        
        LOGGER.info("API testing initialized for environment: {}", EnvironmentManager.getEnvironment());
        LOGGER.info("Browser initialization disabled for API tests");
    }
    
    @BeforeClass
    public void setUp() {
        // Configure REST Assured
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        
        // Initialize API client and config
        apiConfig = ApiConfig.getInstance();
        apiClient = RestAssuredClient.getInstance()
                .setBaseUri(apiConfig.getBaseUrl())
                .setHeaders(apiConfig.getDefaultHeaders());
        
        // Set authorization token if available
        String authToken = apiConfig.getAuthToken();
        if (authToken != null && !authToken.isEmpty()) {
            apiClient.setAuthorizationToken(authToken);
        }
        
        LOGGER.info("API test setup completed with base URL: {}", apiConfig.getBaseUrl());
    }
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        LOGGER.info("Starting API test method: {}", method.getName());
        
        // Get test categories from TestNG annotations if available
        String category = "API";
        if (method.isAnnotationPresent(org.testng.annotations.Test.class)) {
            String[] groups = method.getAnnotation(org.testng.annotations.Test.class).groups();
            if (groups.length > 0) {
                category = String.join(", ", groups);
            }
        }
        
        // Create test in extent report
        extentTest = ExtentManager.getInstance().createTest(method.getName(), "API Test for " + method.getName())
                .assignCategory(category);
        
        extentTest.info("Test Started - Environment: " + EnvironmentManager.getEnvironment());
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail("Test Failed: " + result.getThrowable().getMessage());
            LOGGER.error("Test Failed: {}", result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.pass("Test Passed");
            LOGGER.info("Test Passed");
        } else {
            extentTest.skip("Test Skipped");
            LOGGER.info("Test Skipped");
        }
        
        // Flush the report after each test to ensure it's written to disk
        ExtentManager.flushReport();
    }
    
    @AfterClass
    public void afterClass() {
        // Flush reports at the end of the class
        ExtentManager.flushReport();
        LOGGER.info("API test class completed, reports flushed");
    }
    
    @AfterSuite
    public void afterSuite() {
        // Final flush at the end of the test suite
        ExtentManager.flushReport();
        LOGGER.info("API test suite completed");
        
        // Print the report path for easier access
        String reportPath = ExtentManager.getLastReportPath();
        if (reportPath != null) {
            System.out.println("=================================");
            System.out.println("Extent Report saved to: " + reportPath);
            System.out.println("=================================");
        }
    }
    
    /**
     * Verifies the common success response attributes
     * @param response The response to validate
     * @param expectedStatusCode The expected HTTP status code
     * @return true if validation passes, false otherwise
     */
    protected boolean validateSuccessResponse(Response response, int expectedStatusCode) {
        try {
            ResponseValidator.validateStatusCode(response, expectedStatusCode);
            ResponseValidator.validateContentType(response, "application/json");
            ResponseValidator.validateResponseTime(response, DEFAULT_TIMEOUT_MS);
            extentTest.pass("Response validation successful");
            return true;
        } catch (AssertionError e) {
            extentTest.fail("Response validation failed: " + e.getMessage());
            LOGGER.error("Response validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the endpoint URL for the given key
     * @param endpointKey The endpoint key from properties
     * @return The complete endpoint URL
     */
    protected String getEndpointUrl(String endpointKey) {
        return EnvironmentManager.getEndpoint(endpointKey);
    }
    
    /**
     * Creates query parameters map
     * @param params Key-value pairs for query parameters
     * @return Map of query parameters
     */
    protected Map<String, String> createQueryParams(Object... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Parameters must be in key-value pairs");
        }
        
        Map<String, String> queryParams = new HashMap<>();
        for (int i = 0; i < params.length; i += 2) {
            queryParams.put(params[i].toString(), params[i + 1].toString());
        }
        
        return queryParams;
    }
    
    /**
     * Logs the given response to extent report
     * @param response Response object
     */
    protected void logResponseToReport(Response response) {
        extentTest.info("Response Status Code: " + response.getStatusCode());
        extentTest.info("Response Time: " + response.getTime() + "ms");
        
        String responseBody = response.getBody().asPrettyString();
        if (responseBody.length() > 1000) {
            responseBody = responseBody.substring(0, 1000) + "... (truncated)";
        }
        
        extentTest.info("Response Body: " + responseBody);
    }
} 