package com.example.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",  // Path to your feature files
    glue = {"com.example.registration.steps"},   // Package where your step definitions are located
    plugin = {"pretty", "html:target/cucumber-reports"}, // Reporting plugins
    monochrome = true  // For cleaner console output
)
public class TestRunner {
    // This class remains empty. It is used only as a holder for the above annotations.
}
