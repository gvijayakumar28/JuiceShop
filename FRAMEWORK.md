# JuiceShop Test Automation Framework

A structured, enterprise-level test automation framework built with Java, Selenium, Rest Assured, JUnit 5, Allure Reports, and Jenkins CI/CD.

---

## Technology Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Selenium | 4.27.0 | UI browser automation |
| Rest Assured | 5.5.0 | API testing |
| WebDriverManager | 5.9.2 | Auto-downloads ChromeDriver |
| JUnit 5 | 5.11.3 | Test framework |
| Allure | 2.29.0 | Test reporting |
| SLF4J + Logback | 2.0.16 / 1.5.12 | Logging |
| Gradle | Wrapper | Build and dependency management |
| Jenkins | LTS JDK17 | CI/CD pipeline |
| Docker | Latest | Run JuiceShop and Jenkins |

---

## Project Structure

```
java/  (project root)
│
├── Jenkinsfile                         ← CI/CD pipeline definition
├── build.gradle                        ← dependencies, plugins, Gradle tasks
├── settings.gradle                     ← project name
├── gradlew                             ← run Gradle on Linux/Mac
├── gradlew.bat                         ← run Gradle on Windows
├── .gitignore                          ← excludes test-output/ and build/
├── README.md                           ← original setup instructions
├── FRAMEWORK.md                        ← this file
├── JuiceShopApiReference.md            ← API endpoint documentation
│
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties   ← Gradle version config
│
├── src/test/java/gradle/junit/selenium/
│   │
│   ├── tests/                          ← Test scenarios
│   │   └── JuiceTest.java
│   │
│   ├── base/                           ← Base classes (shared setup)
│   │   ├── BasePage.java
│   │   └── BaseTest.java
│   │
│   ├── pages/                          ← Page Object Model (UI layer)
│   │   ├── LoginPage.java
│   │   ├── ProductListPage.java
│   │   ├── ReviewPage.java
│   │   └── BasketPage.java
│   │
│   ├── api/                            ← API service classes
│   │   ├── AuthApi.java
│   │   └── ProductApi.java
│   │
│   ├── utils/                          ← Reusable utility classes
│   │   ├── ApiClient.java
│   │   ├── DriverFactory.java
│   │   ├── ResponseValidator.java
│   │   ├── ScreenshotUtil.java
│   │   └── ScreenshotOnFailureExtension.java
│   │
│   ├── model/                          ← Data objects / POJOs
│   │   ├── Customer.java
│   │   ├── LoginRequest.java
│   │   └── ReviewRequest.java
│   │
│   └── constants/                      ← Static final values only
│       ├── Endpoints.java
│       └── TestOutputPaths.java
│
├── src/test/resources/
│   ├── logback.xml                     ← Logging configuration
│   └── junit-platform.properties       ← JUnit 5 parallel execution config
│
└── test-output/  (generated, not committed to git)
    ├── allure-results/                 ← Raw Allure JSON data from test run
    ├── reports/                        ← Generated Allure HTML report
    ├── screenshots/                    ← Auto-captured on test failure
    └── logs/
        └── test-run.log                ← Full test execution log
```

---

## Architecture Overview

```
JuiceTest (tests/)
    │
    ├── extends BaseTest (base/)
    │       └── manages WebDriver lifecycle via DriverFactory
    │       └── registers ScreenshotOnFailureExtension
    │
    ├── uses LoginPage / ProductListPage / ReviewPage (pages/)
    │       └── all extend BasePage (base/)
    │               └── provides shared Selenium helpers (click, type, wait)
    │
    └── uses AuthApi / ProductApi (api/)
            └── both use ApiClient (utils/)
                    └── wraps RestAssured for GET/POST/PUT/PATCH/DELETE
                    └── uses ResponseValidator for assertions
```

---

## Package Responsibilities

### `tests/`
Contains only test scenarios. Each test method represents one business scenario.
- `JuiceTest.java` — UI test (login + post review via browser) and API test (login + post review via REST)

### `base/`
Shared base classes that other classes extend.
- `BasePage.java` — abstract class with protected Selenium helper methods (`click`, `type`, `waitForVisible`). All page objects extend this.
- `BaseTest.java` — manages WebDriver lifecycle with `@BeforeAll` and `@AfterAll`. All test classes extend this.

### `pages/`
Page Object Model — one class per page/screen of the application. Hides Selenium locators and actions from test code.
- `LoginPage.java` — open, dismiss popups, login
- `ProductListPage.java` — open first product
- `ReviewPage.java` — submit review, expand reviews, verify review visible
- `BasketPage.java` — open basket, check if items exist

### `api/`
API service classes — one class per domain area.
- `AuthApi.java` — login via REST API, returns auth token
- `ProductApi.java` — search products, post review, verify review

### `utils/`
Reusable tools shared across the whole framework.
- `ApiClient.java` — single HTTP client with `get`, `post`, `put`, `patch`, `delete` methods
- `DriverFactory.java` — thread-safe WebDriver management using `ThreadLocal`
- `ResponseValidator.java` — helper methods for asserting API responses
- `ScreenshotUtil.java` — captures and saves screenshots to `test-output/screenshots/`
- `ScreenshotOnFailureExtension.java` — JUnit 5 extension that auto-screenshots on test failure

### `model/`
POJO classes that hold data. No logic — just fields and constructors.
- `Customer.java` — test user data (email, password, token) with Builder pattern
- `LoginRequest.java` — request body for the login API call
- `ReviewRequest.java` — request body for the post review API call

### `constants/`
Static final values only. Nothing is instantiated from here.
- `Endpoints.java` — all API endpoint paths in one place
- `TestOutputPaths.java` — paths for screenshots, logs, and reports

---

## Design Patterns Used

| Pattern | Where Used | Why |
|---|---|---|
| Page Object Model (POM) | `pages/` package | Separates UI locators from test logic |
| Builder Pattern | `Customer.java` | Creates test data with optional fields and defaults |
| Factory Pattern | `DriverFactory.java` | Centrally creates and manages WebDriver instances |
| ThreadLocal Pattern | `DriverFactory.java` | Makes WebDriver thread-safe for parallel execution |
| Base Class / Inheritance | `BasePage`, `BaseTest` | Avoids duplicate setup/teardown code (DRY principle) |

---

## Key Design Decisions

**Why `BasePage` is abstract?**
It provides helper methods but has no meaning on its own — you can never have just a "base page", you always have a specific page like `LoginPage`. Making it abstract enforces this.

**Why `protected` in `BasePage`?**
`protected` means only subclasses (page objects) can use these helpers — not external classes. This keeps the internal workings of pages hidden from tests.

**Why `ThreadLocal` in `DriverFactory`?**
Each thread in parallel execution gets its own private WebDriver instance. Without `ThreadLocal`, two parallel tests would share one browser and interfere with each other.

**Why POJO classes for API request bodies?**
Using `new LoginRequest(email, password)` instead of raw JSON strings makes the code type-safe, reusable, and avoids typos in field names.

**Why `CI` environment variable for headless mode?**
Tests run with a visible browser locally (easier to debug) and headless inside Jenkins Docker (no display available). The `CI=true` env var switches mode automatically without changing code.

---

## How to Run

### Prerequisites
- Java 17
- Docker Desktop running

### Start JuiceShop
```bash
docker run -d -p 3000:3000 bkimminich/juice-shop:v13.2.0
```

### Run Tests Locally (Windows)
```bash
.\gradlew test
```

### Run Tests Locally (Linux/Mac)
```bash
./gradlew test
```

### Generate Allure Report
```bash
.\gradlew copyAllureReport
```
Open `test-output/reports/index.html` in your browser.

### View Logs
```
test-output/logs/test-run.log
```

### View Screenshots (on failure)
```
test-output/screenshots/
```

---

## Jenkins CI/CD Pipeline

Jenkins pulls code from GitHub and runs the following stages:

```
Checkout → Setup → Run Tests → Generate Report → Copy Report
                                      │
                                  Post Actions
                                  ├── Archive test-output/
                                  ├── Publish JUnit results
                                  └── Publish Allure report tab
```

Jenkins runs inside Docker (`jenkins/jenkins:lts-jdk17`) with:
- Chrome pre-installed for headless test execution
- Project mounted as `/workspace`
- `CI=true` environment variable to enable headless Chrome
- `APP_URL=http://host.docker.internal:3000` to reach JuiceShop on the host machine
