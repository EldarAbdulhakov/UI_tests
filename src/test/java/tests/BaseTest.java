package tests;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {

    private WebDriver driver;
    private WebDriverWait driverWait;

    protected WebDriver getDriver() {
        return driver;
    }

    protected WebDriverWait getDriverWait() {
        if (driverWait == null) {
            driverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        }
        return driverWait;
    }

    @BeforeMethod
    public void setUp() {
        try {
            boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driverWait = null;
        }
    }
}
