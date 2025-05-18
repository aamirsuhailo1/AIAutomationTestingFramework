package com.solutions.it.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonDataReader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    private JsonDataReader() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Reads a JSON file and returns the JsonNode
     * 
     * @param filePath Path to the JSON file
     * @return JsonNode representation of the JSON file
     */
    public static JsonNode readJsonFile(String filePath) {
        try {
            File jsonFile = new File(filePath);
            Log.info("Reading JSON data from file: " + filePath);
            return OBJECT_MAPPER.readTree(jsonFile);
        } catch (IOException e) {
            Log.error("Error reading JSON file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Gets test data for a specific test case from a JSON file
     * 
     * @param filePath Path to the JSON file
     * @param testCaseName The name of the test case to get data for
     * @return Map containing the test data
     */
    public static Map<String, String> getTestData(String filePath, String testCaseName) {
        try {
            JsonNode rootNode = readJsonFile(filePath);
            JsonNode testCaseNode = rootNode.path(testCaseName);
            
            if (testCaseNode.isMissingNode()) {
                Log.error("Test case data not found in JSON file: " + testCaseName);
                throw new RuntimeException("Test case data not found: " + testCaseName);
            }
            
            Map<String, String> testData = new HashMap<>();
            testCaseNode.fields().forEachRemaining(entry -> 
                testData.put(entry.getKey(), entry.getValue().asText()));
            
            return testData;
        } catch (Exception e) {
            Log.error("Error retrieving test data: " + e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve test data for test case: " + testCaseName, e);
        }
    }
} 