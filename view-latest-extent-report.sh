#!/bin/bash

# Script to open the most recent Extent Report for API tests

echo "Looking for latest Extent Report..."

# Ensure the reports directory exists
if [ ! -d "test-output/reports" ]; then
    mkdir -p test-output/reports
    echo "Created reports directory: test-output/reports"
fi

# Find the most recent Extent Report by creation time
# First try to find the specific API test report pattern
EXTENT_REPORT=$(find . -path "*/test-output/reports/*.html" -type f -print0 | xargs -0 ls -t 2>/dev/null | head -1)

if [ -z "$EXTENT_REPORT" ]; then
    # If no report found, try TestNG reports
    EXTENT_REPORT=$(find target/surefire-reports -name "*.html" -type f -print0 | xargs -0 ls -t 2>/dev/null | head -1)
fi

if [ -n "$EXTENT_REPORT" ]; then
    echo "Found latest report: $EXTENT_REPORT"
    echo "Created: $(ls -la "$EXTENT_REPORT" | awk '{print $6, $7, $8}')"
    
    # Open the report based on the operating system
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        open "$EXTENT_REPORT"
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        xdg-open "$EXTENT_REPORT"
    elif [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "win32" ]]; then
        # Windows
        start "$EXTENT_REPORT"
    else
        echo "Cannot open report automatically. Please open manually: $EXTENT_REPORT"
    fi
else
    echo "No HTML reports found. Have you run the tests yet?"
    echo "Check target/surefire-reports or test-output/reports directories."
fi 