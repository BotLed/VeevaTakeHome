package com.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CalculatorTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.get("https://www.calculator.net/carbohydrate-calculator.html");
        driver.manage().window().maximize();
    }

    @Test
        public void testUnitSwitching() {
            WebElement metricTab = driver.findElement(By.xpath("//a[contains(text(),'Metric Units')]"));
            metricTab.click();

            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            
            WebElement heightUnitLabel = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='cheightmeter']/following-sibling::span | //td[contains(.,'cm')]")));

            Assert.assertTrue(heightUnitLabel.getText().contains("cm"), "Unit did not switch to metric (cm)!");
            
            WebElement weightUnitLabel = driver.findElement(By.xpath("//input[@id='ckg']/following-sibling::span | //td[contains(.,'kg')]"));
            Assert.assertTrue(weightUnitLabel.getText().contains("kg"), "Unit did not switch to metric (kg)!");
        }

    @Test
    public void testEmptyFieldsValidation() {
        driver.findElement(By.xpath("//a[contains(text(),'Metric Units')]")).click();
        
        WebElement ageField = driver.findElement(By.name("cage"));
        ageField.clear();

        WebElement calculateBtn = driver.findElement(By.xpath("//input[@value='Calculate']"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", calculateBtn);

        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement errorMsg = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[contains(text(),'Please provide an age between 18 and 80.')]")));
        
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message for age was not displayed.");
    }

    @Test
    public void testClearButtonFunctionality() {
        WebElement ageField = driver.findElement(By.name("cage"));
        ageField.clear();
        ageField.sendKeys("50");
        driver.findElement(By.xpath("//input[@value='Clear']")).click();

        String ageValue = ageField.getAttribute("value");
        Assert.assertNotEquals(ageValue, "50");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
