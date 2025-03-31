package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.sql.DriverManager.getDriver;

public class AddCustomerPage extends BasePage {

    private static String randomPostCode;
    private static String randomFirstName;
    private static final String LAST_NAME = "Petrov";
    private static final String DELETE_CUSTOMER_PATH = "//tbody/tr/td[text()='%s']/..//button[@ng-click='deleteCust(cust)']";
    public static String expectedDeleteName;

    @FindBy(xpath = "//button[@ng-click='addCust()']")
    private WebElement addCustomerButton;

    @FindBy(xpath = "//input[@placeholder='Post Code']")
    private WebElement postCodeField;

    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement addCustomerСonfirmButton;

    @FindBy(xpath = "//button[@ng-click='showCust()']")
    private WebElement customersButton;

    @FindBy(xpath = "//a[contains(@ng-click,'fName')]")
    private WebElement firstNameLink;

    @FindBy(xpath = "//tbody/tr/td[1]")
    private List<WebElement> nameListElements;

    @FindBy(xpath = "//tbody/tr[last()]/td[@class]")
    private List<WebElement> customerData;

    @FindBy(xpath = "//strong[@class='mainHeading']")
    private WebElement logo;

    public AddCustomerPage(WebDriver driver) {
        super(driver);
    }

    private static String getPostCode() {
        Random random = new Random();
        randomPostCode = IntStream.range(0, 10)
                .map(i -> random.nextInt(10))
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
        return randomPostCode;
    }

    private static String getFirstName(String postCode) {
        randomFirstName = "";

        for (int i = 0; i < postCode.length(); i += 2) {
            int digit = Integer.parseInt(postCode.substring(i, i + 2));

            if (digit < 26) {
                randomFirstName += ((char) ('a' + digit));
            } else {
                randomFirstName += ((char) ('a' + (digit % 26)));
            }
        }
        return randomFirstName;
    }

    private static String getRandomPostCode() {
        return randomPostCode;
    }

    private static String getRandomFirstName() {
        return randomFirstName;
    }

    public List<String> getSortFirstNames() {
        return getDriverWait().until(ExpectedConditions.visibilityOfAllElements(nameListElements))
                .stream()
                .map(WebElement::getText)
                .sorted()
                .toList();
    }

    public List<String> getReverseSortFirstNames() {
        return getDriverWait().until(ExpectedConditions.visibilityOfAllElements(nameListElements))
                .stream()
                .map(WebElement::getText)
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    @Step("Wait until open home page")
    public AddCustomerPage waitUntilOpenHomePage() {
        getDriver().get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        return this;
    }

    @Step("Wait until open customers page")
    public AddCustomerPage waitUntilOpenCustomersPage() {
        getDriver().get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager/list");
        return this;
    }

    @Step("Wait until open customers page")
    public AddCustomerPage waitUntilOpenAddCustomerPage() {
        waitUntilOpenHomePage()
                .clickAddCustomerButton();
        return this;
    }

    @Step("Click \"Add Customer\" button")
    public void clickAddCustomerButton() {
        getDriverWait().until(ExpectedConditions.elementToBeClickable(addCustomerButton)).click();
    }

    @Step("Input post code")
    public AddCustomerPage inputPostCode() {
        randomPostCode = getPostCode();
        getDriverWait().until(ExpectedConditions.elementToBeClickable(postCodeField)).sendKeys(randomPostCode);
        return this;
    }

    @Step("Input first name")
    public AddCustomerPage inputFirstName() {
        randomFirstName = getFirstName(randomPostCode);
        firstNameField.sendKeys(randomFirstName);
        return this;
    }

    @Step("Input last name")
    public AddCustomerPage inputLastName() {
        lastNameField.sendKeys(LAST_NAME);
        return this;
    }

    @Step("Click \"Add Customer\" confirm button")
    public AddCustomerPage clickAddCustomerСonfirmButton() {
        addCustomerСonfirmButton.click();
        return this;
    }

    @Step("Click \"OK\" button on alert")
    public AddCustomerPage alertAccept() {
        getDriverWait().until(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().accept();
        return this;
    }

    @Step("Click \"Customers\" button")
    public AddCustomerPage clickCustomersButton() {
        getDriverWait().until(ExpectedConditions.elementToBeClickable(customersButton)).click();
        return this;
    }

    @Step("Click \"First name\" link")
    public AddCustomerPage clickFirstNameLink() {
        getDriverWait().until(ExpectedConditions.elementToBeClickable(firstNameLink)).click();
        return this;
    }

    @Step("Get first names list")
    public List<String> getFirstNamesList() {
        try {
            List<String> firstNamesList = getDriverWait().until(ExpectedConditions.visibilityOfAllElements(nameListElements))
                    .stream()
                    .map(WebElement::getText)
                    .toList();
            if (firstNamesList.isEmpty()) {
                throw new IllegalStateException("The client list is empty");
            }
            return firstNamesList;
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Step("Delete average length name customer")
    public AddCustomerPage deleteAverageLengthNameCustomer() {
        List<String> nameList;
        double averageLengthName;
        String nameToRemove;

        nameList = getFirstNamesList();

        averageLengthName = nameList.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0);

        nameToRemove = nameList.stream()
                .min(Comparator.comparingDouble(name -> Math.abs(name.length() - averageLengthName)))
                .orElse(null);

        expectedDeleteName = nameToRemove;

        getDriver().findElement(By.xpath(DELETE_CUSTOMER_PATH.formatted(nameToRemove))).click();
        return this;
    }

    @Step("Get customer data")
    public List<String> getCustomerData() {
        return getDriverWait().until(ExpectedConditions.visibilityOfAllElements(customerData))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Get expected customer data")
    public static List<String> getExpectedCustomerData() {
        return List.of(getRandomFirstName(), LAST_NAME, getRandomPostCode());
    }
}
