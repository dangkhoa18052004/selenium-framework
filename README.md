# Selenium Framework

[![Test Status](https://github.com/dangkhoa18052004/selenium-framework/actions/workflows/selenium-full.yml/badge.svg)](https://github.com/dangkhoa18052004/selenium-framework/actions)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://dangkhoa18052004.github.io/selenium-framework/)

Automation test framework using:
- Java 17
- Selenium WebDriver
- TestNG
- Maven
- WebDriverManager

## Run local
```bash
mvn clean test -Dbrowser=chrome -DsuiteXmlFile=testng.xml
```

## Notes
- This project is used for Lab 11 CI/CD practice.
- GitHub Actions will run tests automatically on push.
