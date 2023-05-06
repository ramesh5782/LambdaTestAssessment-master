package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;


public class TestLambdaGrid {
    public String username = "rpanthangi";
    public String accesskey = "mFWL1y9Zg9gzh8tZzBO6zxoDphgIpLLA0hwFmgZrGkzt0SoUoP";
    public static RemoteWebDriver driver = null;
    public String gridURL = "@hub.lambdatest.com/wd/hub";
    boolean status = false;

    @BeforeMethod
    @Parameters(value={"browser","version","platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("version", version);
        capabilities.setCapability("platform", platform);
        capabilities.setCapability("build", "RameshBuild");
        capabilities.setCapability("name", "Test01");
        capabilities.setCapability("console", true);
        capabilities.setCapability("network", true);
        capabilities.setCapability("visual", true);

        try {
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
        } catch (MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @org.testng.annotations.Test
    @Parameters(value={"platform"})
    public void testSimple(String platform) throws Exception {

        // Navigate to URL
        driver.get("https://www.lambdatest.com/");

        WebDriverWait wWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='Logo']")));

        // Scroll to LambdaTest Integrations
        WebElement element = driver.findElement(By.xpath("//h2[text()='Seamless Collaboration']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

        // Opening LambdaTest Integrations in New tab
        WebElement lambdaIntegrations = wWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='See All Integrations']")));
        if (platform.contains("mac")) {
            new Actions(driver).keyDown(Keys.COMMAND).click(lambdaIntegrations).keyUp(Keys.COMMAND).build().perform();
        }
        else {
            new Actions(driver).keyDown(Keys.CONTROL).click(lambdaIntegrations).keyUp(Keys.CONTROL).build().perform();
        }
        wWait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Get Window Handles
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        wWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search")));

        // Print Active Windows Handles
        System.out.println("Parent Window Handle Id " + tabs.get(0));
        System.out.println("Child Window Handle Id " + tabs.get(1));

        // Validate LambdaTest Integrations Url"
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://www.lambdatest.com/integrations";

        if (!expectedUrl.equals(actualUrl)) {
            throw new AssertionError("Url validation failed");
        }
        else {
            status = true;
        }

    }


    @AfterMethod
    public void tearDown() throws Exception {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit();
        }
    }
}