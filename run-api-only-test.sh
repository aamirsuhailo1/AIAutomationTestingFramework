#!/bin/bash

# Run API Tests without UI Tests
# Usage: ./run-api-only-test.sh [env]
# Example: ./run-api-only-test.sh qa

# Default environment
ENV=${1:-qa}

echo "=================================="
echo "Running API Tests Only"
echo "Environment: $ENV"
echo "=================================="

# Run JsonPlaceholderApiTests directly with skipBrowser flag
mvn clean test -Dtest=com.solutions.tests.api.JsonPlaceholderApiTests -DskipBrowser=true -Denv=$ENV

# Check if tests ran successfully
if [ $? -eq 0 ]; then
    echo "=================================="
    echo "API Tests Completed Successfully!"
    echo "=================================="
    
    # Open the latest Extent Report
    echo "Opening Extent Report..."
    ./view-latest-extent-report.sh
else
    echo "=================================="
    echo "API Tests Failed!"
    echo "=================================="
    
    # Even for failed tests, we can still view the report
    echo "Opening Extent Report for failed tests..."
    ./view-latest-extent-report.sh
    exit 1
fi 