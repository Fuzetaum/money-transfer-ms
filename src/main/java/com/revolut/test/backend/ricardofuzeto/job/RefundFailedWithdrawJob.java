package com.revolut.test.backend.ricardofuzeto.job;

import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import com.revolut.test.backend.ricardofuzeto.gateway.AccountWSGateway;
import com.revolut.test.backend.ricardofuzeto.model.JobResponsePojo;
import com.revolut.test.backend.ricardofuzeto.model.ResponsePojo;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.revolut.test.backend.ricardofuzeto.database.Tables.TRANSFER;

public class RefundFailedWithdrawJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefundFailedWithdrawJob.class);

    private static final String REFUND_DEVELOPER_MESSAGE = "Total=%d, changed=%d, not_changed=%d";
    private static final String REFUND_USER_MESSAGE = "Withdraws refunded: %d, not refunded: %d, total: %d";

    public static void refundFailedWithdraws() {
        JavalinApp.get("/refund-failed-withdraws", ctx -> {
            LOGGER.info("Job execution started");
            List<Transfer> failedWithdrawList = JooqConfiguration.getDslContext()
                    .select()
                    .from(TRANSFER)
                    .where(TRANSFER.RETRIES_LEFT.eq(0))
                    .fetchInto(Transfer.class);
            JobResponsePojo jobResponsePojo = new JobResponsePojo(failedWithdrawList.size(), 0);
            failedWithdrawList.forEach(failedWithdraw -> {
                if (AccountWSGateway.depositAmountToAccount(failedWithdraw.getReceiver(), failedWithdraw.getAmount())) {
                    jobResponsePojo.setChangedCount(jobResponsePojo.getChangedCount() + 1);
                }
            });
            LOGGER.info("Job execution ended");
            String developerMessage = String.format(REFUND_DEVELOPER_MESSAGE,
                    jobResponsePojo.getTotalCount(),
                    jobResponsePojo.getChangedCount(),
                    jobResponsePojo.getNotChangedCount());
            String userMessage = String.format(REFUND_USER_MESSAGE,
                    jobResponsePojo.getChangedCount(),
                    jobResponsePojo.getNotChangedCount(),
                    jobResponsePojo.getTotalCount());
            return new ResponsePojo(developerMessage, userMessage);
        });
    }
}
