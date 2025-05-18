package com.solutions.it.utils;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
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
} 