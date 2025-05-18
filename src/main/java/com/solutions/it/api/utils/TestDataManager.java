package com.solutions.it.api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDataManager - Manages test data from JSON files
 * Provides methods to load test data for API tests
 */
public class TestDataManager {
    private static final Logger LOGGER = LogManager.getLogger(TestDataManager.class);
    private static final String TEST_DATA_DIR = "src/test/resources/testdata/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private TestDataManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Loads a JSON file and converts it to the specified class
     * @param fileName The JSON file name (without path)
     * @param clazz The class to convert to
     * @param <T> The type of the class
     * @return Object of type T
     */
    public static <T> T loadJsonFile(String fileName, Class<T> clazz) {
        try {
            File file = new File(TEST_DATA_DIR + fileName);
            LOGGER.info("Loading test data from: {}", file.getAbsolutePath());
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            LOGGER.error("Failed to load test data from {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to load test data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Loads a JSON file and converts it to a list of the specified class
     * @param fileName The JSON file name (without path)
     * @param clazz The class element type
     * @param <T> The type of the class
     * @return List of objects of type T
     */
    public static <T> List<T> loadJsonFileAsList(String fileName, Class<T> clazz) {
        try {
            File file = new File(TEST_DATA_DIR + fileName);
            LOGGER.info("Loading test data list from: {}", file.getAbsolutePath());
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            LOGGER.error("Failed to load test data list from {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to load test data list: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets a specific test data object by ID from a JSON file
     * @param fileName The JSON file name (without path)
     * @param idField The field name for the ID
     * @param idValue The ID value to look for
     * @param clazz The class to convert to
     * @param <T> The type of the class
     * @param <I> The type of the ID
     * @return Object of type T or null if not found
     */
    public static <T, I> T getTestDataById(String fileName, String idField, I idValue, Class<T> clazz) {
        try {
            File file = new File(TEST_DATA_DIR + fileName);
            LOGGER.info("Getting test data by ID from: {}, ID field: {}, ID value: {}", 
                    file.getAbsolutePath(), idField, idValue);
            
            JsonNode rootNode = objectMapper.readTree(file);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    if (node.has(idField) && node.get(idField).asText().equals(idValue.toString())) {
                        return objectMapper.convertValue(node, clazz);
                    }
                }
                LOGGER.warn("Test data not found with ID {} in {}", idValue, fileName);
                return null;
            } else {
                LOGGER.error("JSON file {} is not an array", fileName);
                throw new RuntimeException("JSON file is not an array: " + fileName);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to get test data by ID from {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to get test data by ID: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts an object to JSON string
     * @param object The object to convert
     * @return JSON string
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.error("Failed to convert object to JSON string: {}", e.getMessage());
            throw new RuntimeException("Failed to convert object to JSON string: " + e.getMessage(), e);
        }
    }
} 