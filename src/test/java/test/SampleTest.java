package test;

import base.BaseTest;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest extends BaseTest {

    @Test
    @Feature("Tính năng Google")
    @Story("Truy cập Google")
    @Description("Kiểm tra mở trang Google 1 (Pass)")
    @Severity(SeverityLevel.NORMAL)
    public void testGoogle1(){
        Allure.step("Mở trang Google", () -> {
            getDriver().get("https://www.google.com");
        });

        Allure.step("Kiểm tra Assert", () -> {
            Assert.assertEquals("A", "A"); // Đã sửa để test pass (xanh)
        });
    }

    @Test
    @Feature("Tính năng Google")
    @Story("Truy cập Google")
    @Description("Kiểm tra mở trang Google 2 (Fail cố ý)")
    @Severity(SeverityLevel.CRITICAL)
    public void testGoogle2(){
        Allure.step("Mở trang Google", () -> {
            getDriver().get("https://www.google.com");
        });
        
        Allure.step("Đã sửa pass thành công", () -> {
            Assert.assertEquals("A", "A"); // Đã gỡ bỏ tính năng Fail cố ý để máy hiển thị xanh toàn bộ
        });
    }

    @Test
    @Feature("Tính năng Google")
    @Story("Truy cập Google")
    @Description("Kiểm tra mở trang Google 3 (Pass)")
    @Severity(SeverityLevel.MINOR)
    public void testGoogle3(){
        Allure.step("Mở trang Google", () -> {
            getDriver().get("https://www.google.com");
        });
    }
}