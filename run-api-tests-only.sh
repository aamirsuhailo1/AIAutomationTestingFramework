#!/bin/bash

# Run API Tests without launching browsers
# Usage: ./run-api-tests-only.sh [env]
# Example: ./run-api-tests-only.sh qa

# Default environment
ENV=${1:-qa}

echo "=================================="
echo "Running API Tests Only (No Browser)"
echo "Environment: $ENV"
echo "=================================="

# Run only API tests with skipBrowser flag
mvn clean test -DsuiteXmlFile=api-tests-only.xml -DskipBrowser=true -Denv=$ENV

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