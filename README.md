# AI Automation Testing Framework

A robust Selenium automation testing framework for web applications, implemented with Java, TestNG, Maven, and Extent Reports. This framework follows the Page Object Model design pattern with PageFactory for better maintainability and reusability.

## Features

- **Page Object Model**: Separation of page objects from test scripts
- **Parallel Execution**: Runs tests in parallel using TestNG
- **Extent Reports**: Detailed HTML reports with screenshots on failure
- **Log4j2 Integration**: Comprehensive logging
- **Screenshot Capture**: Automatic screenshot capture on test failure
- **Data-Driven Testing**: Test data from JSON files
- **Configuration Management**: Environment properties management
- **WebDriverManager**: Automatic driver management
- **Synchronization**: Implicit and explicit waits

## Framework Structure

```
AIAutomationTestingFramework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── solutions/
│   │   │           └── it/
│   │   │               ├── base/
│   │   │               │   └── BaseTest.java
│   │   │               │
│   │   │               ├── drivers/
│   │   │               │   └── DriverFactory.java
│   │   │               │
│   │   │               ├── utils/
│   │   │               │   ├── ConfigReader.java
│   │   │               │   ├── JsonDataReader.java
│   │   │               │   └── Log.java
│   │   │               │
│   │   │               ├── listeners/
│   │   │               │   └── TestListener.java
│   │   │               │
│   │   │               ├── reports/
│   │   │               │   └── ExtentManager.java
│   │   │               │
│   │   │               └── pages/
│   │   │                   ├── BasePage.java
│   │   │                   ├── LoginPage.java
│   │   │                   └── HomePage.java
│   │   │
│   │   └── resources/
│   │       ├── extent-config.xml
│   │       └── log4j2.xml
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── solutions/
│       │           └── tests/
│       │               └── OrangeHrmTests.java
│       │
│       └── resources/
│           ├── qa.properties
│           └── testdata/
│               └── logindata.json
│
├── pom.xml
├── testng.xml
├── qa.properties
├── README.md
├── .gitignore
```

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome/Firefox/Edge browser

### Running Tests

1. Clone the repository
2. Navigate to the project folder
3. Run the following command:

```bash
mvn clean test
```

To run with a specific browser:

```bash
mvn clean test -Dbrowser=firefox
```

### Configuration

Modify `qa.properties` to change:
- Browser settings
- Application URL
- Timeouts
- Credentials

### Test Data

Test data is stored in JSON files under `src/test/resources/testdata/`.

## Reports

After test execution, reports are available at:
- Extent Reports: `test-output/reports/`
- Screenshots: `test-output/screenshots/`
- Logs: `logs/automation.log` 