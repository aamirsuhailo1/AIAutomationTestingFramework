package com.solutions.it.pages;

import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    
    @FindBy(xpath = "//span[@class='oxd-userdropdown-tab']")
    private WebElement userDropdown;
    
    @FindBy(xpath = "//h6[contains(@class,'oxd-topbar-header-breadcrumb')]")
    private WebElement dashboardTitle;
    
    @FindBy(xpath = "//a[contains(@href, 'viewMyDetails')]")
    private WebElement myInfoLink;
    
    @FindBy(xpath = "//a[contains(@href, 'pim')]")
    private WebElement pimLink;
    
    @FindBy(xpath = "//a[contains(@href, 'leave')]")
    private WebElement leaveLink;
    
    @FindBy(xpath = "//a[contains(@href, 'viewAdminModule')]")
    private WebElement adminLink;
    
    @FindBy(xpath = "//ul[@class='oxd-dropdown-menu']/li/a[text()='Logout']")
    private WebElement logoutLink;
    
    // Additional element to verify successful login
    @FindBy(xpath = "//p[@class='oxd-userdropdown-name']")
    private WebElement userNameText;
    
    public HomePage(WebDriver driver) {
        super(driver);
        try {
            waitForPageLoad();
            // Add additional explicit wait for dashboard elements to be visible
            wait.until(ExpectedConditions.visibilityOf(userDropdown));
            Log.info("Home page loaded successfully");
        } catch (Exception e) {
            Log.error("Error while waiting for Home page to load: " + e.getMessage());
        }
    }
    
    public boolean isLoaded() {
        try {
            // Wait for user dropdown to be visible with a shorter timeout
            boolean userDropdownVisible = isElementDisplayed(userDropdown);
            boolean dashboardTitleVisible = false;
            
            // Only check dashboard title if user dropdown is visible
            if (userDropdownVisible) {
                dashboardTitleVisible = isElementDisplayed(dashboardTitle);
            }
            
            Log.info("HomePage elements status - userDropdown: " + userDropdownVisible + 
                    ", dashboardTitle: " + dashboardTitleVisible);
            
            return userDropdownVisible;
        } catch (Exception e) {
            Log.error("Error checking if home page is loaded: " + e.getMessage());
            return false;
        }
    }
    
    public String getDashboardTitle() {
        try {
            waitForElementToBeVisible(dashboardTitle);
            return getText(dashboardTitle);
        } catch (Exception e) {
            Log.error("Error getting dashboard title: " + e.getMessage());
            return "";
        }
    }
    
    public HomePage clickOnPIM() {
        Log.info("Clicking on PIM link");
        click(pimLink);
        return this;
    }
    
    public HomePage clickOnMyInfo() {
        Log.info("Clicking on My Info link");
        click(myInfoLink);
        return this;
    }
    
    public HomePage clickOnLeave() {
        Log.info("Clicking on Leave link");
        click(leaveLink);
        return this;
    }
    
    public HomePage clickOnAdmin() {
        Log.info("Clicking on Admin link");
        click(adminLink);
        return this;
    }
    
    public void logout() {
        Log.info("Performing logout");
        click(userDropdown);
        waitForElementToBeVisible(logoutLink);
        click(logoutLink);
    }
} 