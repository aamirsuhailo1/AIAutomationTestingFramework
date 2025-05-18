# Setting Up Jenkins Integration for Automation Framework

This guide explains how to set up Jenkins to run the Selenium automation tests.

## Prerequisites

1. Jenkins server installed and running
2. Maven plugin installed in Jenkins
3. JDK 11 configured in Jenkins
4. Git plugin installed in Jenkins
5. HTML Publisher plugin installed in Jenkins

## Jenkins Configuration Steps

### 1. Configure JDK and Maven in Jenkins

1. Go to **Dashboard** > **Manage Jenkins** > **Global Tool Configuration**
2. Configure JDK:
   - Name: `JDK11`
   - JAVA_HOME: Path to your JDK 11 installation
3. Configure Maven:
   - Name: `Maven3`
   - MAVEN_HOME: Path to your Maven installation

### 2. Create a Jenkins Pipeline Job

1. From Jenkins dashboard, click **New Item**
2. Enter a name for your job (e.g., `SeleniumAutomation`)
3. Select **Pipeline** and click **OK**
4. In the configuration page:
   - **Description**: Add a meaningful description
   - **Pipeline**:
     - Select **Pipeline script from SCM**
     - **SCM**: Select **Git**
     - **Repository URL**: Enter your Git repository URL
     - **Branch Specifier**: `*/master` (or the branch containing your Jenkinsfile)
     - **Script Path**: `Jenkinsfile`
5. Click **Save**

### 3. Set up Scheduled Runs (Optional)

1. Go to your pipeline job configuration
2. Check **Build Periodically**
3. Enter a cron expression, for example:
   - `0 0 * * *` (runs daily at midnight)
   - `0 0 * * 1-5` (runs on weekdays at midnight)

### 4. Configure Email Notifications (Optional)

1. Go to **Dashboard** > **Manage Jenkins** > **System Configuration**
2. Configure email notifications 
3. In your pipeline job configuration, add email notification in the post section of the Jenkinsfile

### 5. Run the Pipeline

1. Go to your pipeline job
2. Click **Build Now**
3. Monitor the build progress in the **Build History**
4. Click on a build to see details and access:
   - Console Output
   - Test Results
   - Extent Reports
   - Failure Screenshots

## Handling Selenium WebDriver in Jenkins

For headless execution in Jenkins:

1. Modify `qa.properties` file:
   ```
   headless=true
   ```

2. Add Chrome/Firefox options in the DriverFactory class:
   - Chrome: `chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");`
   - Firefox: `firefoxOptions.addArguments("--headless");`

## Troubleshooting

### Common Issues and Solutions

1. **Browser Initialization Failures**:
   - Ensure the correct WebDriver version is available
   - Add `--no-sandbox` and `--disable-dev-shm-usage` for Chrome
   - Set headless mode on

2. **Path Issues**:
   - Use absolute paths or ensure relative paths are resolved correctly
   - Ensure test-output directory has correct permissions

3. **Jenkins Agent Issues**:
   - Ensure the agent has all required software installed
   - Use Docker-based agents with pre-configured environments

## Further Improvements

1. Add Docker support for consistent test environments
2. Implement test result trends and analytics
3. Set up different environments (DEV, QA, PROD) using parameters
4. Add Slack/MS Teams notifications 