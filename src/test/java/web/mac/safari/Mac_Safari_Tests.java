package web.mac.safari;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import io.perfecto.utils.PerfectoTokenStorage;
import io.perfecto.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Mac_Safari_Tests {
  private RemoteWebDriver driver;
  private ReportiumClient reportiumClient;
  private WebDriverWait wait;

  @BeforeClass
  public void setUpDriver() throws Exception {
    String host = "demo";
    SafariOptions browserOptions = new SafariOptions();

    browserOptions.setPlatformName("Mac");
    browserOptions.setBrowserVersion("16");

    Map<String, Object> perfectoOptions = new HashMap<>();
    perfectoOptions.put("platformVersion", "macOS Ventura");
    perfectoOptions.put("location", "NA-US-BOS");
    perfectoOptions.put("resolution", "1920x1080");
    perfectoOptions.put("securityToken", PerfectoTokenStorage.getTokenForCloud(host));

    browserOptions.setCapability("perfecto:options", perfectoOptions);

    driver = new RemoteWebDriver(new URL("https://" + host + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), browserOptions);

    PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
        .withProject(new Project("Perfecto.Support Mac Web tests", "1.0"))
        .withJob(new Job("Perfecto.Support Mac Web tests", 1))
        .withWebDriver(driver)
        .build();
    reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

    wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    System.out.println(browserOptions);
    System.out.println(driver);
  }

  @AfterMethod
  public void testEnded(ITestResult result) {

    if (result.getStatus() == ITestResult.FAILURE) {
      reportiumClient.testStop(TestResultFactory.createFailure(result.getThrowable()));
      return;
    }
    reportiumClient.testStop(TestResultFactory.createSuccess());
  }

  @AfterClass
  public void tearDown() {
    driver.quit();
    Utils.openReportUrl(reportiumClient.getReportUrl());
  }

  @Test
  public void webTestOnMacOnSafari1() throws Exception {
    reportiumClient.testStart("Web Test on Mac Safari Open DuckDuckGo", new TestContext());
    driver.get("https://duckduckgo.com/");

    WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchbox_input")));
    el.sendKeys("Perfecto");

    driver.findElement(By.cssSelector("button[type=submit]"))
        .click();
    Thread.sleep(5000);

    driver.getScreenshotAs(OutputType.FILE);
  }

  @Test
  public void webTestOnMacOnSafari2() throws Exception {
    reportiumClient.testStart("Web Test on Mac Safari Open Google", new TestContext());
    driver.get("https://google.com/");
    WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
    el.sendKeys("Perfecto");

    driver.findElement(By.name("btnK"))
        .click();

    Thread.sleep(5000);

    driver.getScreenshotAs(OutputType.FILE);
  }
}
