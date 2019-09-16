package com.revolut.test.backend.ricardofuzeto.job;

import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import com.revolut.test.backend.ricardofuzeto.gateway.AccountWSGateway;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import com.revolut.test.backend.ricardofuzeto.service.RequestErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.revolut.test.backend.ricardofuzeto.database.Tables.PENDING_WITHDRAW;
import static com.revolut.test.backend.ricardofuzeto.database.Tables.TRANSFER;

public class RefundFailedWithdrawJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefundFailedWithdrawJob.class);

    public static void refundFailedWithdraws() {
        JavalinApp.get("/refund-failed-withdraws", ctx -> {
            LOGGER.info("Job execution started");
            List<Transfer> failedWithdrawList = JooqConfiguration.getDslContext()
                    .select()
                    .from(TRANSFER)
                    .where(TRANSFER.RETRIESLEFT.eq(0))
                    .fetchInto(Transfer.class);
            failedWithdrawList.forEach(failedWithdraw -> {
                AccountWSGateway.depositAmountToAccount(failedWithdraw.getReceiver(), failedWithdraw.getAmount());
            });
            LOGGER.info("Job execution ended");
            return new RequestErrorResponse("" + failedWithdrawList.size(), "Withdraws refunded: " + failedWithdrawList.size());
        });
    }
}
