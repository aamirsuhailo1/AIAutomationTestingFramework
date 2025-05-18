package com.solutions.it.pages;

import com.solutions.it.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    
    @FindBy(name = "username")
    private WebElement usernameInput;
    
    @FindBy(name = "password")
    private WebElement passwordInput;
    
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;
    
    @FindBy(xpath = "//p[contains(@class, 'oxd-alert-content-text')]")
    private WebElement errorMessage;
    
    @FindBy(xpath = "//div[contains(@class, 'orangehrm-login-branding')]")
    private WebElement loginLogo;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isLoaded() {
        waitForPageLoad();
        return isElementDisplayed(loginLogo) && isElementDisplayed(usernameInput);
    }
    
    public LoginPage enterUsername(String username) {
        Log.info("Entering username: " + username);
        sendKeys(usernameInput, username);
        return this;
    }
    
    public LoginPage enterPassword(String password) {
        Log.info("Entering password");
        sendKeys(passwordInput, password);
        return this;
    }
    
    public HomePage clickLoginButton() {
        Log.info("Clicking login button");
        click(loginButton);
        return new HomePage(driver);
    }
    
    public String getErrorMessage() {
        if (isElementDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }
} 