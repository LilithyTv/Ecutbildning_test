package com.exempel.registration.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class RegistrationSteps {
    private WebDriver driver;
    private final String BROWSER = "chrome";



    @Before
    public void setUp() throws MalformedURLException {
        String browser = System.getProperty("browser", BROWSER);
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Usuario\\IdeaProjects\\Ecutbildning_test\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-features=NetworkService");
            options.addArguments("--disable-extensions");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-debugging-port=9222");
            options.setExperimentalOption("useAutomationExtension", false);
            driver = new RemoteWebDriver(new URL("http://localhost:9515"), new ChromeOptions());
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "C:\\Drivers\\geckodriver.exe");
            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);


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

        driver.findElement(By.id("dp")).sendKeys(detailsList.get(5).get("value"));
        driver.findElement(By.id("member_firstname")).sendKeys(detailsList.get(0).get("value"));
        driver.findElement(By.id("member_lastname")).sendKeys(detailsList.get(1).get("value"));
        driver.findElement(By.id("member_emailaddress")).sendKeys(detailsList.get(2).get("value"));
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(detailsList.get(2).get("value"));
        driver.findElement(By.id("signupunlicenced_password")).sendKeys(detailsList.get(3).get("value"));
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys(detailsList.get(4).get("value"));
    }

    @When("I enter registration details:")
    public void enterRegistrationDetailsGeneric(DataTable dataTable) {
        List<Map<String, String>> detailsList = dataTable.asMaps(String.class, String.class);

        Map<String, String> detailsMap = detailsList.stream()
                .filter(row -> row.get("field") != null) // ignore rows with null field
                .collect(Collectors.toMap(
                        row -> row.get("field"),
                        row -> row.get("value") != null ? row.get("value") : "", // replace null with empty string if needed
                        (existing, replacement) -> existing  // in case of duplicate keys, keep the first one
                ));


        if (detailsMap.containsKey("birthDate")) {
            driver.findElement(By.id("dp")).sendKeys(detailsMap.get("birthDate"));
        }
        if (detailsMap.containsKey("firstName")) {
            driver.findElement(By.id("member_firstname")).sendKeys(detailsMap.get("firstName"));
        }
        if (detailsMap.containsKey("lastName")) {
            driver.findElement(By.id("member_lastname")).sendKeys(detailsMap.get("lastName"));
        }
        if (detailsMap.containsKey("email")) {
            driver.findElement(By.id("member_emailaddress")).sendKeys(detailsMap.get("email"));
            driver.findElement(By.id("member_confirmemailaddress")).sendKeys(detailsMap.get("email"));
        }
        if (detailsMap.containsKey("password")) {
            driver.findElement(By.id("signupunlicenced_password")).sendKeys(detailsMap.get("password"));
        }
        if (detailsMap.containsKey("confirmPassword")) {
            driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys(detailsMap.get("confirmPassword"));
        }

    }

    @When("I accept the terms and conditions")
    public void acceptTermsAndConditions() {
        WebElement termsCheckbox = driver.findElement(
                By.xpath("/html/body/div/div[2]/div/div/div/div/div/div/div/form/div[11]/div/div[2]/div[1]/label/span[3]"));
        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }

        WebElement notUnderageCheckBox = driver.findElement(
                By.xpath("/html/body/div/div[2]/div/div/div/div/div/div/div/form/div[11]/div/div[2]/div[2]/label/span[3]"));
        if (!notUnderageCheckBox.isSelected()) {
            notUnderageCheckBox.click();
        }

        WebElement termsEthicsCheckBox = driver.findElement(
                By.xpath("/html/body/div/div[2]/div/div/div/div/div/div/div/form/div[11]/div/div[7]/label/span[3]"));
        if (!termsEthicsCheckBox.isSelected()) {
            termsEthicsCheckBox.click();
        }
    }

    @When("I set the terms and conditions acceptance to {word}")
    public void setTermsAndConditionsAcceptance(String termsAccepted) {
        boolean accept = Boolean.parseBoolean(termsAccepted);
        WebElement termsCheckbox = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div/div/div/div/div/form/div[11]/div/div[7]/label/span[3]"));
        if (accept && !termsCheckbox.isSelected()) {
            termsCheckbox.click();
        } else if (!accept && termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
    }

    @When("I submit the registration form")
    public void submitRegistrationForm() {
        driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div/div/div/div/div/form/div[12]/input")).click();
    }

    @Then("I should see a success message")
    public void verifySuccessMessage() {
        WebElement successMsg = waitForElement(By.xpath("/html/body/div/div[2]/div/h2"));
        Assert.assertTrue("Success message is not displayed", successMsg.isDisplayed());
    }

    @Then("I should see an error message {string}")
    public void verifyErrorMessage(String expectedError) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Wait until the page source contains the expected error message
        boolean errorAppeared = wait.until(driver -> driver.getPageSource().contains(expectedError));
        Assert.assertTrue("Expected error message not found. Expected to contain: " + expectedError, errorAppeared);
    }

    
    private WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
