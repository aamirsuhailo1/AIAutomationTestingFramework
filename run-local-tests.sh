#!/bin/bash

echo "Docker not detected. Running tests locally instead."
echo "Note: This is a fallback option and doesn't provide the isolation benefits of Docker."

# Set environment variables to use local WebDriver
export SELENIUM_REMOTE=false

# Create directory for test outputs if it doesn't exist
mkdir -p test-output/reports
mkdir -p test-output/screenshots

# Run the tests locally
echo "Running tests locally..."
mvn clean test

echo "Tests completed. Results are available in the test-output directory." 