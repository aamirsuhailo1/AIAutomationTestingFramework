#!/bin/bash

# Stop any existing containers
docker-compose down

# Build and start containers
docker-compose up -d selenium-hub chrome firefox

# Wait for the grid to be ready
echo "Waiting for Selenium Grid to be ready..."
sleep 10

# Run tests
echo "Running tests in Docker..."
docker-compose run test

# Gather test results
echo "Tests completed. Results are available in test-output directory."

# Keep containers running unless specified
if [ "$1" == "--down" ]; then
    echo "Stopping containers..."
    docker-compose down
fi 