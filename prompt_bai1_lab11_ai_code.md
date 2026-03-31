# Prompt AI code Bài 1 - Lab 11

## Vai trò
Bạn là AI coding assistant. Hãy chỉnh sửa project Selenium Java hiện có để hoàn thành **Bài 1 – GitHub Actions CI/CD cơ bản** của Lab 11.

## Bối cảnh
- Repo hiện tại: `selenium-framework`
- Đây là project từ Lab 9
- Hiện **chưa có** file `DriverFactory.java`
- Mục tiêu: khi push code lên GitHub thì Selenium test tự chạy bằng GitHub Actions
- CI phải chạy được trên Ubuntu GitHub Actions nên browser phải hỗ trợ **headless mode**

## Yêu cầu đầu ra
Hãy:
1. Phân tích cấu trúc project hiện tại
2. Tạo hoặc chỉnh sửa các file cần thiết
3. Viết code hoàn chỉnh, không để placeholder
4. Ưu tiên giữ tương thích với cấu trúc project hiện có
5. Nếu tên package hiện tại khác ví dụ mẫu, hãy tự điều chỉnh import/package cho đúng project

---

## Mục tiêu Bài 1
Hoàn thành pipeline CI/CD cơ bản:
- Mỗi lần push lên GitHub thì test tự chạy
- Có file workflow GitHub Actions
- Có hỗ trợ headless cho môi trường CI
- Có upload artifact gồm test report và screenshot nếu có

---

## Các file cần tạo hoặc chỉnh sửa

### 1. Tạo file `.gitignore`
Nếu chưa có thì tạo mới ở thư mục gốc với nội dung tối thiểu:

```gitignore
target/
.idea/
*.iml
screenshots/
.env
*.log
```

Nếu repo đang track thư mục `target/` thì hướng dẫn git bỏ track thư mục này.

---

### 2. Cập nhật `README.md`
Cập nhật README ngắn gọn, rõ ràng, gồm:
- tên project
- công nghệ sử dụng
- cách chạy local bằng Maven
- ghi chú project dùng để thực hành CI/CD

Mẫu mong muốn:

```md
# Selenium Framework

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
```

---

### 3. Tạo file workflow GitHub Actions
Tạo file:

```text
.github/workflows/selenium-ci.yml
```

Nội dung cần đáp ứng:
- trigger khi push vào `main` hoặc `develop`
- trigger khi pull request vào `main`
- có `workflow_dispatch`
- dùng Java 17
- chạy Maven test
- upload artifact:
  - `target/surefire-reports/`
  - `target/screenshots/`

Dùng workflow này làm chuẩn, nhưng nếu project đang dùng file suite khác thì tự chỉnh cho đúng:

```yaml
name: Selenium Test Suite

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

jobs:
  run-tests:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup Java 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Run Selenium Tests
        run: mvn clean test -Dbrowser=chrome -DsuiteXmlFile=testng.xml

      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: |
            target/surefire-reports/
            target/screenshots/
          retention-days: 30
```

**Lưu ý:** Nếu project đang dùng `testng-smoke.xml` thì dùng file đó. Nếu chỉ có `testng.xml` thì giữ `testng.xml`.

---

### 4. Tạo mới `DriverFactory.java`
Vì project hiện chưa có file này, hãy tạo mới file ở vị trí hợp lý, ưu tiên:

```text
src/main/java/driver/DriverFactory.java
```

Yêu cầu:
- hỗ trợ `chrome` và `firefox`
- tự bật headless khi chạy trên CI bằng:
  - `boolean isCI = System.getenv("CI") != null;`
- Chrome trên CI cần các option:
  - `--headless=new`
  - `--no-sandbox`
  - `--disable-dev-shm-usage`
  - `--window-size=1920,1080`
- Local có thể dùng `--start-maximized`
- dùng WebDriverManager

Code mong muốn:

```java
package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        boolean isCI = System.getenv("CI") != null;

        switch (browser.toLowerCase()) {
            case "firefox":
                return createFirefoxDriver(isCI);
            default:
                return createChromeDriver(isCI);
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }
}
```

Nếu package `driver` không phù hợp với project hiện tại thì đặt ở package phù hợp hơn, nhưng phải đồng bộ import.

---

### 5. Chỉnh sửa `BaseTest.java` hoặc nơi đang khởi tạo driver
Tìm nơi project hiện đang tạo driver trực tiếp, ví dụ:
- `BaseTest.java`
- `Hooks.java`
- test class cha
- utility class quản lý browser

Nếu có dòng kiểu:
```java
driver = new ChromeDriver();
```

thì thay bằng:
```java
driver = DriverFactory.createDriver(System.getProperty("browser", "chrome"));
```

Yêu cầu thêm:
- giữ tương thích với test hiện tại
- không phá vỡ luồng setup/teardown cũ
- nếu project đang dùng ThreadLocal hoặc pattern khác thì tích hợp khéo léo thay vì rewrite toàn bộ

---

### 6. Nếu project chưa có screenshot khi fail thì thêm
Nếu chưa có logic screenshot khi fail, hãy bổ sung vào `BaseTest` hoặc utility hiện có.

Yêu cầu:
- khi test fail thì lưu ảnh vào:
```text
target/screenshots/
```
- tên file nên chứa tên test và timestamp nếu cần
- nếu project đã có logic tương đương thì giữ lại, chỉ cần bảo đảm workflow upload đúng thư mục

Ví dụ logic mong muốn:

```java
@AfterMethod
public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        takeScreenshot(result.getName());
    }
    if (driver != null) {
        driver.quit();
    }
}
```

Và hàm chụp ảnh tương ứng.

---

### 7. Kiểm tra `pom.xml`
Kiểm tra để chắc chắn project có đủ dependency/plugin cần thiết:
- Selenium
- TestNG
- WebDriverManager
- Maven Surefire Plugin

Nếu thiếu dependency phục vụ screenshot như `commons-io` thì bổ sung tối thiểu, tránh sửa dư thừa.

---

## Yêu cầu triển khai
- Không đổi cấu trúc project quá nhiều
- Không tạo thêm kiến trúc lớn ngoài phạm vi Bài 1
- Chỉ sửa những gì cần để chạy CI/CD cơ bản
- Nếu cần tạo package mới thì phải hợp lý và tối thiểu
- Code phải build được

---

## Tiêu chí hoàn thành
Sau khi code xong, project phải đáp ứng:
1. Có file `.github/workflows/selenium-ci.yml`
2. Có `DriverFactory.java`
3. Driver chạy được local và CI
4. GitHub Actions có thể chạy test bằng Maven
5. Có artifact test reports và screenshots
6. Dễ tạo 1 test fail cố ý để lấy log đỏ phục vụ nộp bài

---

## Đầu ra mong muốn từ AI
Hãy trả về theo format:
1. Danh sách file đã tạo / đã sửa
2. Nội dung code hoàn chỉnh của từng file
3. Giải thích ngắn lý do sửa
4. Các lệnh git cần chạy:
```bash
git add .
git commit -m "Complete Lab 11 exercise 1"
git push origin main
```

---

## Gợi ý bổ sung
Nếu phát hiện project đang dùng tên package hoặc suite file khác với ví dụ:
- hãy tự điều chỉnh cho khớp
- ưu tiên làm cho project chạy được thực tế hơn là bám cứng ví dụ

Nếu có nhiều nơi khởi tạo driver:
- hãy gom về `DriverFactory`
- nhưng chỉ refactor tối thiểu cần thiết cho Bài 1
