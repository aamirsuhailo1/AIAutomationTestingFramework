package com.solutions.tests;

import com.solutions.it.base.BaseTest;
import com.solutions.it.pages.HomePage;
import com.solutions.it.pages.LoginPage;
import com.solutions.it.utils.JsonDataReader;
import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class OrangeHrmTests extends BaseTest {
    
    private static final String TEST_DATA_FILE = "src/test/resources/testdata/logindata.json";
    
    @Test(description = "Verify user can login with valid credentials")
    public void testValidLogin() {
        Log.info("Starting valid login test");
        
        // Get test data
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, "validLoginTest");
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
    
    @Test(description = "Verify error message for invalid login")
    public void testInvalidLogin() {
        Log.info("Starting invalid login test");
        
        // Get test data
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, "invalidLoginTest");
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
    
    @Test(description = "Verify user can navigate to Admin page")
    public void testNavigateToAdminPage() {
        Log.info("Starting navigation to Admin page test");
        
        // Get test data
        Map<String, String> testData = JsonDataReader.getTestData(TEST_DATA_FILE, "adminNavigationTest");
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