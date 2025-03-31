package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddCustomerPage;

import java.util.List;

@Feature("Actions with the client")
public class CustomerTest extends BaseTest {

    @Test
    @Description("Checking the addition of a new client")
    public void testAddCustomer() {
        List<String> customerData = new AddCustomerPage(getDriver())
                .waitUntilOpenAddCustomerPage()
                .inputPostCode()
                .inputFirstName()
                .inputLastName()
                .clickAddCustomerСonfirmButton()
                .alertAccept()
                .clickCustomersButton()
                .getCustomerData();

        Assert.assertEquals(customerData, AddCustomerPage.getExpectedCustomerData());
    }

    @Test
    @Description("Checking sorting in reverse alphabetical order by name")
    public void testReverseSortFirstNames() {
        List<String> customerNamesReverseSort = new AddCustomerPage(getDriver())
                .waitUntilOpenCustomersPage()
                .clickFirstNameLink()
                .getFirstNamesList();

        Assert.assertEquals(customerNamesReverseSort, (new AddCustomerPage(getDriver())).getReverseSortFirstNames());
    }

    @Test
    @Description("Checking sorting in alphabetical order by name")
    public void testSortFirstNames() {
        List<String> customerNamesSort = new AddCustomerPage(getDriver())
                .waitUntilOpenCustomersPage()
                .clickFirstNameLink()
                .clickFirstNameLink()
                .getFirstNamesList();

        Assert.assertEquals(customerNamesSort, (new AddCustomerPage(getDriver())).getSortFirstNames());
    }

    @Test
    @Description("Check for deletion of a client with a name length approximating the arithmetic mean of the lengths of all names")
    public void testDeleteAverageLengthNameCustomer() {
        List<String> customerNamesAfterDelete = new AddCustomerPage(getDriver())
                .waitUntilOpenCustomersPage()
                .deleteAverageLengthNameCustomer()
                .getFirstNamesList();

        Assert.assertListNotContainsObject(customerNamesAfterDelete, AddCustomerPage.expectedDeleteName, AddCustomerPage.expectedDeleteName);
    }
}