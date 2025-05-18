#!/bin/bash

# Check if Docker is installed and running
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed or not in your PATH"
    echo "Please install Docker from https://docs.docker.com/get-docker/"
    exit 1
fi

# Check Docker status
docker info &> /dev/null
if [ $? -ne 0 ]; then
    echo "Docker is not running. Please start the Docker daemon."
    exit 1
fi

echo "Starting Selenium Grid container..."
docker run -d -p 4444:4444 -p 4442:4442 -p 4443:4443 --name selenium-hub selenium/hub:4.18.1

echo "Starting Chrome node..."
docker run -d -p 5901:5900 --link selenium-hub:hub -e SE_EVENT_BUS_HOST=hub \
    -e SE_EVENT_BUS_PUBLISH_PORT=4442 \
    -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 \
    -e SE_NODE_MAX_SESSIONS=5 \
    --shm-size=2g \
    --name selenium-chrome \
    selenium/node-chrome:4.18.1

echo "Waiting for Grid to be ready..."
sleep 10

echo "Building and running tests..."
# First, create a temporary Dockerfile without the volume mount
cat > Dockerfile.temp << EOF
FROM maven:3.8-openjdk-11

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Configure environment variables for Docker
ENV SELENIUM_GRID_URL=http://selenium-hub:4444/wd/hub
ENV SELENIUM_REMOTE=true

# Run tests
CMD ["mvn", "test"]
EOF

# Build the Docker image
docker build -t selenium-tests -f Dockerfile.temp .

# Run the tests
docker run --link selenium-hub:selenium-hub --name test-runner selenium-tests

# Copy test results
echo "Copying test results from the container..."
docker cp test-runner:/app/test-output .

echo "Cleaning up..."
docker rm test-runner
docker rm -f selenium-chrome
docker rm -f selenium-hub

echo "Tests completed. Results are available in the test-output directory." 