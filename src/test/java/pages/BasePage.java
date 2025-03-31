package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    private final WebDriver driver;
    private WebDriverWait driverWait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected WebDriverWait getDriverWait() {
        if (driverWait == null) {
            driverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        }
        return driverWait;
    }
}