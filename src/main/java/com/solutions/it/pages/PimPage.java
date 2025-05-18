package com.solutions.it.pages;

import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PimPage extends BasePage {

    @FindBy(xpath = "//h6[text()='PIM']")
    private WebElement pimHeader;
    
    @FindBy(xpath = "//button[contains(@class, 'oxd-button--secondary') and normalize-space(text())='Add']")
    private WebElement addEmployeeButton;
    
    @FindBy(xpath = "//div[@class='oxd-table-filter']")
    private WebElement searchPanel;
    
    @FindBy(xpath = "//label[contains(text(), 'Employee Name')]/../..//input")
    private WebElement employeeNameInput;
    
    @FindBy(xpath = "//label[contains(text(), 'Employee Id')]/../..//input")
    private WebElement employeeIdInput;
    
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;
    
    @FindBy(xpath = "//div[@class='oxd-table-body']/div[contains(@class, 'oxd-table-card')]")
    private WebElement firstEmployeeRecord;
    
    @FindBy(xpath = "//div[@class='oxd-table-cell-actions']/button[1]") // Edit button
    private WebElement editEmployeeButton;
    
    @FindBy(xpath = "//h6[text()='Personal Details']")
    private WebElement personalDetailsHeader;
    
    @FindBy(xpath = "//input[@name='firstName']")
    private WebElement firstNameInput;
    
    @FindBy(xpath = "//input[@name='lastName']")
    private WebElement lastNameInput;
    
    @FindBy(xpath = "//input[@name='middleName']")
    private WebElement middleNameInput;
    
    @FindBy(xpath = "//h6[text()='Personal Details']/../..//button[@type='submit']")
    private WebElement saveButton;
    
    @FindBy(xpath = "//div[contains(@class, 'oxd-toast-container')]//p[contains(@class, 'oxd-toast-content-text')]")
    private WebElement toastMessage;
    
    public PimPage(WebDriver driver) {
        super(driver);
        try {
            waitForPageLoad();
            wait.until(ExpectedConditions.visibilityOf(pimHeader));
            Log.info("PIM page loaded successfully");
        } catch (Exception e) {
            Log.error("Error while waiting for PIM page to load: " + e.getMessage());
        }
    }
    
    public boolean isLoaded() {
        return isElementDisplayed(pimHeader);
    }
    
    public PimPage searchEmployeeById(String id) {
        Log.info("Searching for employee with ID: " + id);
        waitForElementToBeVisible(searchPanel);
        waitForElementToBeClickable(employeeIdInput);
        employeeIdInput.clear();
        sendKeys(employeeIdInput, id);
        click(searchButton);
        waitForPageLoad();
        return this;
    }
    
    public PimPage searchEmployeeByName(String name) {
        Log.info("Searching for employee with name: " + name);
        waitForElementToBeVisible(searchPanel);
        waitForElementToBeClickable(employeeNameInput);
        employeeNameInput.clear();
        sendKeys(employeeNameInput, name);
        click(searchButton);
        waitForPageLoad();
        return this;
    }
    
    public boolean isEmployeeFound() {
        try {
            waitForElementToBeVisible(firstEmployeeRecord);
            return true;
        } catch (Exception e) {
            Log.info("No employee records found");
            return false;
        }
    }
    
    public PimPage clickOnFirstEmployeeEdit() {
        Log.info("Clicking on edit button for the first employee");
        waitForElementToBeVisible(firstEmployeeRecord);
        waitForElementToBeClickable(editEmployeeButton);
        click(editEmployeeButton);
        waitForElementToBeVisible(personalDetailsHeader);
        return this;
    }
    
    public String getEmployeeFirstName() {
        return firstNameInput.getAttribute("value");
    }
    
    public String getEmployeeLastName() {
        return lastNameInput.getAttribute("value");
    }
    
    public PimPage updateEmployeeDetails(String firstName, String middleName, String lastName) {
        Log.info("Updating employee details: " + firstName + " " + middleName + " " + lastName);
        waitForElementToBeVisible(personalDetailsHeader);
        
        // Clear and set first name
        firstNameInput.clear();
        sendKeys(firstNameInput, firstName);
        
        // Clear and set middle name if provided
        if (middleName != null && !middleName.isEmpty()) {
            middleNameInput.clear();
            sendKeys(middleNameInput, middleName);
        }
        
        // Clear and set last name
        lastNameInput.clear();
        sendKeys(lastNameInput, lastName);
        
        // Save the changes
        click(saveButton);
        
        // Wait for success message
        try {
            waitForElementToBeVisible(toastMessage);
            Log.info("Update message: " + getText(toastMessage));
        } catch (Exception e) {
            Log.error("Could not find toast message: " + e.getMessage());
        }
        
        return this;
    }
} 