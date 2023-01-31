package selenium.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestBase {
    public static WebDriver driver;

    @BeforeEach
    public void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");

        ChromeOptions handlingSSL = new ChromeOptions();
        handlingSSL.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(handlingSSL);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }
}
