package com.solutions.it.utils;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for providing test data to test methods.
 * Integrates with JsonDataReader for data-driven testing.
 */
public class DataProviderUtils {
    private static final String TEST_DATA_FILE = "src/test/resources/testdata/logindata.json";
    
    /**
     * Data provider for login related tests
     * 
     * @param method the test method
     * @return Object[][] containing test data
     */
    @DataProvider(name = "loginData", parallel = true)
    public static Object[][] getLoginData(Method method) {
        String methodName = method.getName();
        String testCaseName = "";
        
        // Map method names to test data names
        switch (methodName) {
            case "testValidLogin":
                testCaseName = "validLoginTest";
                break;
            case "testInvalidLogin":
                testCaseName = "invalidLoginTest";
                break;
            case "testEmptyPasswordLogin":
                testCaseName = "emptyPasswordTest";
                break;
            default:
                Log.warn("No test data mapping found for method: " + methodName);
                return new Object[][]{{}};
        }
        
        // Get test data from JSON file
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, testCaseName);
        return new Object[][]{{testData}};
    }
    
    /**
     * Data provider for navigation related tests
     * 
     * @param method the test method
     * @return Object[][] containing test data
     */
    @DataProvider(name = "navigationData", parallel = true)
    public static Object[][] getNavigationData(Method method) {
        String methodName = method.getName();
        String testCaseName = "";
        
        // Map method names to test data names
        switch (methodName) {
            case "testNavigateToAdminPage":
                testCaseName = "adminNavigationTest";
                break;
            case "testNavigateToPimPage":
                testCaseName = "pimNavigationTest";
                break;
            case "testNavigateToMyInfoPage":
                testCaseName = "myInfoNavigationTest";
                break;
            default:
                Log.warn("No test data mapping found for method: " + methodName);
                return new Object[][]{{}};
        }
        
        // Get test data from JSON file
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, testCaseName);
        return new Object[][]{{testData}};
    }
    
    /**
     * Data provider for employee search test
     * 
     * @param method the test method
     * @return Object[][] containing test data
     */
    @DataProvider(name = "employeeSearchData", parallel = true)
    public static Object[][] getEmployeeSearchData(Method method) {
        // Get test data from JSON file
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, "employeeSearchTest");
        return new Object[][]{{testData}};
    }
    
    /**
     * Data provider for cross-browser testing.
     * Combines test data with browser information for running tests across multiple browsers.
     *
     * @param method the test method
     * @return Object[][] containing test data and browser information
     */
    @DataProvider(name = "crossBrowserData", parallel = true)
    public static Object[][] getCrossBrowserData(Method method) {
        String methodName = method.getName();
        String testCaseName = mapMethodToTestCase(methodName);
        
        // Get test data for the method
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, testCaseName);
        
        // Supported browsers - changed to Chrome and Safari only
        String[] browsers = {"chrome", "safari"};
        
        // Create data combinations
        List<Object[]> testCombinations = new ArrayList<>();
        for (String browser : browsers) {
            Map<String, Object> combinedData = new HashMap<>(testData);
            combinedData.put("browser", browser);
            testCombinations.add(new Object[]{combinedData});
        }
        
        return testCombinations.toArray(new Object[0][0]);
    }
    
    /**
     * Maps a method name to the corresponding test case name
     * 
     * @param methodName the test method name
     * @return the test case name
     */
    private static String mapMethodToTestCase(String methodName) {
        switch (methodName) {
            case "testValidLogin":
                return "validLoginTest";
            case "testInvalidLogin":
                return "invalidLoginTest";
            case "testEmptyPasswordLogin":
                return "emptyPasswordTest";
            case "testNavigateToAdminPage":
                return "adminNavigationTest";
            case "testNavigateToPimPage":
                return "pimNavigationTest";
            case "testNavigateToMyInfoPage":
                return "myInfoNavigationTest";
            default:
                Log.warn("No test data mapping found for method: " + methodName);
                return "";
        }
    }
} 