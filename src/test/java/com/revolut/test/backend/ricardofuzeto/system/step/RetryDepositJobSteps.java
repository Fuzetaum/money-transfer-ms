package com.revolut.test.backend.ricardofuzeto.system.step;

import com.revolut.test.backend.ricardofuzeto.configuration.Environment;
import com.revolut.test.backend.ricardofuzeto.configuration.EnvironmentTestUtils;
import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.PendingDeposit;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import com.revolut.test.backend.ricardofuzeto.utils.HttpRequestPojo;
import com.revolut.test.backend.ricardofuzeto.utils.HttpRequestUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.revolut.test.backend.ricardofuzeto.database.Tables.PENDING_DEPOSIT;
import static com.revolut.test.backend.ricardofuzeto.database.tables.Transfer.TRANSFER;
import static com.revolut.test.backend.ricardofuzeto.database.tables.TransferAttempt.TRANSFER_ATTEMPT;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RetryDepositJobSteps {
    private static Logger LOGGER = LoggerFactory.getLogger(RetryDepositJobSteps.class);
    private static HttpRequestPojo response;

    @Before("@RetryDepositJob")
    public void setUp() throws InterruptedException {
        EnvironmentTestUtils.configure();
        response = null;
    }

    @After("@RetryDepositJob")
    public void tearDown() {
        JooqConfiguration.getDslContext()
                .deleteFrom(PENDING_DEPOSIT)
                .execute();
        JooqConfiguration.getDslContext()
                .deleteFrom(TRANSFER_ATTEMPT)
                .execute();
        JooqConfiguration.getDslContext()
                .deleteFrom(TRANSFER)
                .execute();
    }

    @Given("^there are no pending deposits$")
    public void thereAreNoPendingWithdraws() {}

    @Given("^the following deposits are pending$")
    public void theFollowingWithdrawsArePending(List<Transfer> transfers) {
        transfers.forEach(transfer -> {
            JooqConfiguration.getTransferDao()
                    .insert(transfer);
            JooqConfiguration.getPendingDepositDao()
                    .insert(new PendingDeposit(transfer.getId()));
        });
    }

    @When("^RetryDepositJob runs$")
    public void retryWithdrawJobRuns() throws IOException {
        String url = "http://localhost:" + Environment.get(Environment.PORT) + "/retry-deposits";
        String requestBody = "{\"replicas\": 1, \"instance\": 1}";
        response = HttpRequestUtils.sendPost(url, requestBody);
    }

    @Then("^system will report that no deposits were processed$")
    public void systemWillReportThatNoWithdrawsWereProcessed() {
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getResponse(), containsString("\"developerMessage\": \"0\""));
        assertThat(response.getResponse(), containsString("\"userMessage\": \"Deposits reattempted: 0\""));
    }

    @Then("^system will report that (\\d+) deposits were processed$")
    public void systemWillReportThatWithdrawsWereProcessed(Integer amount) {
        assertThat(response.getStatus(), is(equalTo(200)));
        assertThat(response.getResponse(), containsString("\"developerMessage\": \"" + amount + "\""));
        assertThat(response.getResponse(), containsString("\"userMessage\": \"Deposits reattempted: " + amount + "\""));
    }
}
