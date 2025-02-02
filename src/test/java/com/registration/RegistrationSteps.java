package com.example.registration.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegistrationSteps {
    private WebDriver driver;

    @Before
    public void setUp() {
        // Use system property "browser" to decide which browser to use (default is Chrome)
        String browser = System.getProperty("browser", "chrome");
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");
            driver = new FirefoxDriver();
        } else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I navigate to the registration page")
    public void navigateToRegistrationPage() {
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    @When("I enter valid registration details:")
    public void enterValidRegistrationDetails(DataTable dataTable) {
        List<Map<String, String>> detailsList = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = detailsList.get(0);
        
        driver.findElement(By.id("firstName")).sendKeys(details.get("firstName"));
        driver.findElement(By.id("lastName")).sendKeys(details.get("lastName"));
        driver.findElement(By.id("email")).sendKeys(details.get("email"));
        driver.findElement(By.id("password")).sendKeys(details.get("password"));
        driver.findElement(By.id("confirmPassword")).sendKeys(details.get("confirmPassword"));
    }

    @When("I enter registration details:")
    public void enterRegistrationDetailsGeneric(DataTable dataTable) {
        // This method is used for both valid and negative scenarios.
        List<Map<String, String>> detailsList = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = detailsList.get(0);

        if (details.containsKey("firstName")) {
            driver.findElement(By.id("firstName")).sendKeys(details.get("firstName"));
        }
        if (details.containsKey("lastName")) {
            driver.findElement(By.id("lastName")).sendKeys(details.get("lastName"));
        }
        if (details.containsKey("email")) {
            driver.findElement(By.id("email")).sendKeys(details.get("email"));
        }
        if (details.containsKey("password")) {
            driver.findElement(By.id("password")).sendKeys(details.get("password"));
        }
        if (details.containsKey("confirmPassword")) {
            driver.findElement(By.id("confirmPassword")).sendKeys(details.get("confirmPassword"));
        }
    }

    @When("I accept the terms and conditions")
    public void acceptTermsAndConditions() {
        // Assuming there's a checkbox with id "terms"
        WebElement termsCheckbox = driver.findElement(By.id("terms"));
        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
    }

    @When("I set the terms and conditions acceptance to {string}")
    public void setTermsAndConditionsAcceptance(String termsAccepted) {
        // Convert the string to a boolean
        boolean accept = Boolean.parseBoolean(termsAccepted);
        WebElement termsCheckbox = driver.findElement(By.id("terms"));
        if (accept && !termsCheckbox.isSelected()) {
            termsCheckbox.click();
        } else if (!accept && termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
    }

    @When("I submit the registration form")
    public void submitRegistrationForm() {
        // Adjust the locator for the submit button as needed (example: id "submit")
        driver.findElement(By.id("submit")).click();
    }

    @Then("I should see a success message")
    public void verifySuccessMessage() {
        // Wait for the success message element to be visible and verify it is displayed
        WebElement successMsg = waitForElement(By.id("successMessage"));
        Assert.assertTrue("Success message is not displayed", successMsg.isDisplayed());
    }

    @Then("I should see an error message {string}")
    public void verifyErrorMessage(String expectedError) {
        // Wait for the error message element and verify it contains the expected text
        WebElement errorMsg = waitForElement(By.id("errorMessage"));
        String actualText = errorMsg.getText();
        Assert.assertTrue("Expected error message not found. Expected to contain: " + expectedError,
                actualText.contains(expectedError));
    }
    
    // Private helper method using explicit wait
    private WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
