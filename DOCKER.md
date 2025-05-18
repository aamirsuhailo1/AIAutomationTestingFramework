# Docker Setup for Test Automation Framework

This framework supports running tests inside Docker containers using Selenium Grid, which provides several benefits:

- Consistent test environment across different machines
- Isolated test execution
- Easy parallel testing with multiple browser types
- No need to install browsers or drivers locally

## Prerequisites

- Docker installed on your machine
- Docker Compose installed on your machine

## Quick Start

To run the tests in Docker, simply execute:

```bash
./run-docker-tests.sh
```

This script will:
1. Start Selenium Grid Hub and browser nodes (Chrome and Firefox)
2. Build the test container with the framework
3. Run tests against the Selenium Grid
4. Preserve the containers for inspection (add `--down` to stop them after tests)

## Manual Setup

If you prefer to run commands individually:

1. Start Selenium Grid and browser containers:
   ```bash
   docker-compose up -d selenium-hub chrome firefox
   ```

2. Run the tests:
   ```bash
   docker-compose run test
   ```

3. When finished, stop all containers:
   ```bash
   docker-compose down
   ```

## Configuration

Docker-specific configuration is managed through environment variables in the `docker-compose.yml` file:

- `SELENIUM_GRID_URL`: The URL of the Selenium Grid Hub
- `SELENIUM_REMOTE`: Flag to enable remote WebDriver execution

## Viewing Test Execution

The Selenium nodes support VNC connections for viewing test execution:

- Chrome node: Connect to `localhost:5901` with a VNC viewer (password: `secret`)
- Firefox node: Connect to `localhost:5902` with a VNC viewer (password: `secret`)

## Test Reports

Test reports are available in the `test-output` directory, which is mounted as a volume in the Docker container. This means reports are accessible locally even after the container is stopped.

## Troubleshooting

### Connection Issues

If tests can't connect to the Selenium Grid:
1. Check if the Grid is running: `docker-compose ps`
2. Ensure Grid is healthy: `docker logs selenium-hub`
3. Verify network connectivity: `docker-compose exec test curl -I selenium-hub:4444`

### Missing Browser Support

If you need additional browsers:
1. Add the browser service to `docker-compose.yml`
2. Update the WebDriverManager class to support the new browser

### Slow Test Execution

If tests are running slowly in Docker:
1. Increase memory and CPU allocation to Docker
2. Adjust container resource limits in `docker-compose.yml`
3. Consider using shared memory volumes for browser containers 