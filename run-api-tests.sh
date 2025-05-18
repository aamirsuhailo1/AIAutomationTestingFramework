#!/bin/bash

# Run API Tests with specified environment
# Usage: ./run-api-tests.sh [env] [groups]
# Example: ./run-api-tests.sh qa smoke,regression

# Default values
ENV=${1:-qa}
GROUPS=${2:-api}

echo "=================================="
echo "Running API Tests"
echo "Environment: $ENV"
echo "Test Groups: $GROUPS"
echo "=================================="

# Set environment variable for the test
export ENV=$ENV

# Run tests with specified groups
mvn clean test -DsuiteXmlFile=api-tests.xml -Dgroups=$GROUPS -Denv=$ENV

# Check if tests ran successfully
if [ $? -eq 0 ]; then
    echo "=================================="
    echo "API Tests Completed Successfully!"
    echo "=================================="
    
    # Open test report
    echo "Opening Test Report..."
    REPORT_PATH="test-output/ExtentReports/ExtentReport.html"
    
    if [ -f "$REPORT_PATH" ]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            # macOS
            open "$REPORT_PATH"
        elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
            # Linux
            xdg-open "$REPORT_PATH"
        elif [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "win32" ]]; then
            # Windows
            start "$REPORT_PATH"
        else
            echo "Cannot open report automatically. Please open manually: $REPORT_PATH"
        fi
    else
        echo "Report not found at: $REPORT_PATH"
    fi
else
    echo "=================================="
    echo "API Tests Failed!"
    echo "=================================="
    exit 1
fi 