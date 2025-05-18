package com.solutions.it.pages;

import com.solutions.it.utils.ConfigReader;
import com.solutions.it.utils.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait shortWait; // For operations that should timeout faster
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        
        // Configure standard and shorter waits
        long explicitWaitSeconds = Long.parseLong(ConfigReader.getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSeconds));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 5 second wait for quick checks
        
        PageFactory.initElements(driver, this);
    }
    
    protected void waitForElementToBeClickable(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Element not clickable: " + element.toString(), e);
            }
        }
    }
    
    protected void waitForElementToBeVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Element not visible: " + element.toString(), e);
            }
        }
    }
    
    // Quick check with shorter timeout - returns boolean instead of throwing exception
    protected boolean waitForElementToBeVisibleQuick(WebElement element) {
        try {
            shortWait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            if (Log.getLogger().isWarnEnabled()) {
                Log.warn("Element not quickly visible (using short wait): " + element.toString());
            }
            return false;
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Error checking element visibility: " + element.toString(), e);
            }
            return false;
        }
    }
    
    protected void click(WebElement element) {
        waitForElementToBeClickable(element);
        try {
            element.click();
            if (Log.getLogger().isInfoEnabled()) {
                Log.info("Clicked on element: " + element.toString());
            }
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Failed to click element: " + element.toString(), e);
            }
            
            try {
                // Try JavaScript click as fallback
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
                
                if (Log.getLogger().isInfoEnabled()) {
                    Log.info("Clicked element using JavaScript: " + element.toString());
                }
            } catch (Exception jsException) {
                if (Log.getLogger().isErrorEnabled()) {
                    Log.error("Failed to click element using JavaScript: " + element.toString(), jsException);
                }
                // Preserve the original exception's stack trace
                jsException.addSuppressed(e);
                throw jsException;
            }
        }
    }
    
    protected void sendKeys(WebElement element, String text) {
        waitForElementToBeVisible(element);
        try {
            element.clear();
            element.sendKeys(text);
            if (Log.getLogger().isInfoEnabled()) {
                Log.info("Entered text '" + text + "' in element: " + element.toString());
            }
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Failed to enter text in element: " + element.toString(), e);
            }
            throw e;
        }
    }
    
    protected String getText(WebElement element) {
        waitForElementToBeVisible(element);
        try {
            String text = element.getText();
            if (Log.getLogger().isInfoEnabled()) {
                Log.info("Got text '" + text + "' from element: " + element.toString());
            }
            return text;
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Failed to get text from element: " + element.toString(), e);
            }
            throw e;
        }
    }
    
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return waitForElementToBeVisibleQuick(element);
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    protected void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            if (Log.getLogger().isInfoEnabled()) {
                Log.info("Scrolled to element: " + element.toString());
            }
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Failed to scroll to element: " + element.toString(), e);
            }
        }
    }
    
    protected void waitForPageLoad() {
        try {
            wait.until(webDriver -> "complete".equals(
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
            
            if (Log.getLogger().isInfoEnabled()) {
                Log.info("Page loaded completely");
            }
            
            // Additional wait for AJAX completions (if any)
            try {
                Thread.sleep(500); // Small delay to allow for any final AJAX completions
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            if (Log.getLogger().isErrorEnabled()) {
                Log.error("Error waiting for page to load: " + e.getMessage());
            }
        }
    }
} 