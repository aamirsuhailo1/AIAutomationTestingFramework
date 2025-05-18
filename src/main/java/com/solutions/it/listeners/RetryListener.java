package com.solutions.it.listeners;

import com.solutions.it.utils.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * RetryListener for automatically applying the RetryAnalyzer to test methods.
 */
public class RetryListener implements IAnnotationTransformer {
    
    /**
     * Transforms test annotations to apply the RetryAnalyzer
     * 
     * @param annotation the test annotation
     * @param testClass the test class
     * @param testConstructor the test constructor
     * @param testMethod the test method
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Set the retry analyzer for the test method
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
} 