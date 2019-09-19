package com.revolut.test.backend.ricardofuzeto.system.step;

import com.revolut.test.backend.ricardofuzeto.configuration.EnvironmentTestUtils;
import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.job.RetryWithdrawJob;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import static com.revolut.test.backend.ricardofuzeto.database.tables.PendingWithdraw.PENDING_WITHDRAW;

public class RetryWithdrawJobSteps {
    private static TestLogger JOB_LOGGER = TestLoggerFactory.getTestLogger(RetryWithdrawJob.class);
    private static TestLogger JAVALIN_LOGGER = TestLoggerFactory.getTestLogger(JavalinApp.class);

    @Before
    public void setUp() {
        EnvironmentTestUtils.configure();
    }

    @SuppressWarnings("unused")
    @Given("^there are no pending withdraws$")
    public void thereAreNoPendingWithdraws() {
        JooqConfiguration.getDslContext()
                .deleteFrom(PENDING_WITHDRAW)
                .execute();
    }

    @SuppressWarnings("unused")
    @When("^RetryWithdrawJob runs$")
    public void testWhen() {
    }

    @SuppressWarnings("unused")
    @Then("^system will report that no withdraws were processed$")
    public void testThen() {
        System.out.println(JAVALIN_LOGGER.getLoggingEvents().toString());
        JAVALIN_LOGGER.getLoggingEvents().forEach(event -> System.out.println(event.getMessage()));
    }
}
