#!/bin/bash

# Colors for better output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}======================================${NC}"
echo -e "${YELLOW}    CROSS-BROWSER TEST EXECUTION      ${NC}"
echo -e "${YELLOW}======================================${NC}"

# Clean and compile the project
echo -e "${GREEN}Cleaning and compiling project...${NC}"
mvn clean compile

# Run cross-browser tests
echo -e "${GREEN}Running cross-browser tests...${NC}"
mvn test -DsuiteXmlFile=cross-browser-testng.xml

# Check if tests ran successfully
if [ $? -eq 0 ]; then
  echo -e "${GREEN}Cross-browser tests completed successfully!${NC}"
  echo -e "${GREEN}View results in the test-output directory.${NC}"
else
  echo -e "${RED}Cross-browser tests failed. Check logs for details.${NC}"
fi

echo -e "${YELLOW}======================================${NC}" 