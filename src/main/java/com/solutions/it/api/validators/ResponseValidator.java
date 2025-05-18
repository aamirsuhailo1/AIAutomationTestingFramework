package com.solutions.it.api.validators;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * ResponseValidator - Utility class for validating API responses
 * Contains reusable methods for common response validations
 */
public class ResponseValidator {
    private static final Logger LOGGER = LogManager.getLogger(ResponseValidator.class);
    
    /**
     * Validates status code in response
     * @param response Response object
     * @param expectedStatusCode Expected status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        LOGGER.info("Validating status code: expected={}, actual={}", expectedStatusCode, actualStatusCode);
        Assert.assertEquals(actualStatusCode, expectedStatusCode, 
                "Expected status code " + expectedStatusCode + " but found " + actualStatusCode);
    }
    
    /**
     * Validates response time is within acceptable limits
     * @param response Response object
     * @param maxResponseTimeMs Maximum acceptable response time in milliseconds
     */
    public static void validateResponseTime(Response response, long maxResponseTimeMs) {
        long responseTime = response.getTime();
        LOGGER.info("Validating response time: max={}ms, actual={}ms", maxResponseTimeMs, responseTime);
        Assert.assertTrue(responseTime <= maxResponseTimeMs, 
                "Response time " + responseTime + "ms exceeds maximum acceptable time " + maxResponseTimeMs + "ms");
    }
    
    /**
     * Validates content type in response
     * @param response Response object
     * @param expectedContentType Expected content type
     */
    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        LOGGER.info("Validating content type: expected={}, actual={}", expectedContentType, actualContentType);
        Assert.assertTrue(actualContentType.contains(expectedContentType), 
                "Expected content type " + expectedContentType + " but found " + actualContentType);
    }
    
    /**
     * Validates response body contains specific field
     * @param response Response object
     * @param fieldPath JSON path to the field
     */
    public static void validateFieldExists(Response response, String fieldPath) {
        LOGGER.info("Validating field exists: {}", fieldPath);
        Assert.assertNotNull(response.jsonPath().get(fieldPath), 
                "Field " + fieldPath + " not found in response");
    }
    
    /**
     * Validates response body field value
     * @param response Response object
     * @param fieldPath JSON path to the field
     * @param expectedValue Expected value
     */
    public static void validateFieldValue(Response response, String fieldPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(fieldPath);
        LOGGER.info("Validating field value: field={}, expected={}, actual={}", fieldPath, expectedValue, actualValue);
        Assert.assertEquals(actualValue, expectedValue, 
                "Expected value " + expectedValue + " for field " + fieldPath + " but found " + actualValue);
    }
    
    /**
     * Validates response body contains array with specified size
     * @param response Response object
     * @param arrayPath JSON path to the array
     * @param expectedSize Expected array size
     */
    public static void validateArraySize(Response response, String arrayPath, int expectedSize) {
        List<?> array = response.jsonPath().getList(arrayPath);
        int actualSize = array.size();
        LOGGER.info("Validating array size: path={}, expected={}, actual={}", arrayPath, expectedSize, actualSize);
        Assert.assertEquals(actualSize, expectedSize, 
                "Expected array size " + expectedSize + " for " + arrayPath + " but found " + actualSize);
    }
    
    /**
     * Validates response headers
     * @param response Response object
     * @param expectedHeaders Map of expected header name-value pairs
     */
    public static void validateHeaders(Response response, Map<String, String> expectedHeaders) {
        LOGGER.info("Validating response headers: {}", expectedHeaders);
        expectedHeaders.forEach((headerName, expectedValue) -> {
            String actualValue = response.getHeader(headerName);
            Assert.assertEquals(actualValue, expectedValue, 
                    "Expected header " + headerName + " to be " + expectedValue + " but found " + actualValue);
        });
    }
    
    /**
     * Validates JSON schema compliance
     * @param response Response object
     * @param schemaPath Path to the JSON schema file
     */
    public static void validateJsonSchema(Response response, String schemaPath) {
        LOGGER.info("Validating JSON schema: {}", schemaPath);
        response.then().assertThat().body(io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
        LOGGER.info("JSON schema validation successful");
    }
    
    /**
     * Validates error response
     * @param response Response object
     * @param expectedStatusCode Expected status code
     * @param errorMessagePath JSON path to error message
     */
    public static void validateErrorResponse(Response response, int expectedStatusCode, String errorMessagePath) {
        validateStatusCode(response, expectedStatusCode);
        validateFieldExists(response, errorMessagePath);
        LOGGER.info("Error message: {}", response.jsonPath().getString(errorMessagePath));
    }
    
    /**
     * Performs basic validation on successful response
     * @param response Response object
     * @param expectedStatusCode Expected status code
     */
    public static void validateSuccessResponse(Response response, int expectedStatusCode) {
        validateStatusCode(response, expectedStatusCode);
        validateContentType(response, "application/json");
        LOGGER.info("Basic success response validation completed");
    }
} 