package com.revolut.test.backend.ricardofuzeto.system.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryWithdrawJobSteps {
    private static Logger LOGGER = LoggerFactory.getLogger(RetryWithdrawJobSteps.class);

    @Given("^there is no pending withdraws$")
    public void testGiven() {
        LOGGER.info("Given");
    }

    @When("^RetryWithdrawJob runs$")
    public void testWhen() {
        LOGGER.info("When");
    }

    @Then("^system will report that no withdraws were processed$")
    public void testThen() {
        LOGGER.info("Then");
    }
}
