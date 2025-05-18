package com.solutions.tests;

import com.solutions.it.base.BaseTest;
import com.solutions.it.pages.HomePage;
import com.solutions.it.pages.LoginPage;
import com.solutions.it.utils.DataProviderUtils;
import com.solutions.it.utils.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Test class for OrangeHRM application.
 * Contains tests for login and navigation functionality.
 */
public class OrangeHrmTests extends BaseTest {
    
    /**
     * Verifies that a user can login with valid credentials
     * 
     * @param testData test data from data provider
     */
    @Test(description = "Verify user can login with valid credentials", 
          dataProvider = "loginData", 
          dataProviderClass = DataProviderUtils.class)
    public void testValidLogin(Map<String, String> testData) {
        Log.info("Starting valid login test");
        
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedTitle = testData.get("expectedTitle");
        
        // Initialize login page
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoaded(), "Login page is not loaded properly");
        
        // Perform login
        HomePage homePage = loginPage.login(username, password);
        
        // Verify successful login
        Assert.assertTrue(homePage.isLoaded(), "Home page is not loaded after login");
        Assert.assertTrue(homePage.getDashboardTitle().contains(expectedTitle),
                "Dashboard title does not match expected: " + expectedTitle);
        
        Log.info("Valid login test completed successfully");
    }
    
    /**
     * Verifies error message for invalid login
     * 
     * @param testData test data from data provider
     */
    @Test(description = "Verify error message for invalid login", 
          dataProvider = "loginData", 
          dataProviderClass = DataProviderUtils.class)
    public void testInvalidLogin(Map<String, String> testData) {
        Log.info("Starting invalid login test");
        
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedErrorMessage = testData.get("expectedErrorMessage");
        
        // Initialize login page
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoaded(), "Login page is not loaded properly");
        
        // Perform login with invalid credentials
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        
        // Verify error message
        String actualErrorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
                "Error message does not match expected: " + expectedErrorMessage);
        
        Log.info("Invalid login test completed successfully");
    }
    
    /**
     * Verifies navigation to Admin page
     * 
     * @param testData test data from data provider
     */
    @Test(description = "Verify user can navigate to Admin page", 
          dataProvider = "navigationData", 
          dataProviderClass = DataProviderUtils.class)
    public void testNavigateToAdminPage(Map<String, String> testData) {
        Log.info("Starting navigation to Admin page test");
        
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedHeading = testData.get("expectedHeading");
        
        // Initialize login page and perform login
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login(username, password);
        
        // Verify successful login
        Assert.assertTrue(homePage.isLoaded(), "Home page is not loaded after login");
        
        // Navigate to Admin page
        homePage.clickOnAdmin();
        
        // Verify navigation to Admin page by checking page title
        String pageTitle = homePage.getDashboardTitle();
        Assert.assertTrue(pageTitle.contains(expectedHeading),
                "Page title does not contain expected heading: " + expectedHeading);
        
        Log.info("Navigation to Admin page test completed successfully");
    }
} 