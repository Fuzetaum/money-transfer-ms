package com.revolut.test.backend.ricardofuzeto.job;

import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.TransferAttempt;
import com.revolut.test.backend.ricardofuzeto.model.ResponsePojo;
import com.revolut.test.backend.ricardofuzeto.model.TransferResult;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import com.revolut.test.backend.ricardofuzeto.service.TransferService;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.revolut.test.backend.ricardofuzeto.database.Tables.PENDING_WITHDRAW;
import static com.revolut.test.backend.ricardofuzeto.database.Tables.TRANSFER;

public class RetryWithdrawJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryWithdrawJob.class);

    public static void retryWithdraws() {
        JavalinApp.get("/retry-withdraws", ctx -> {
            LOGGER.info("Job execution started");
            List<String> idList = JooqConfiguration.getDslContext()
                    .select()
                    .from(PENDING_WITHDRAW)
                    .where(TRANSFER.RETRIESLEFT.greaterThan(0))
                    .fetch()
                    .getValues(PENDING_WITHDRAW.ID, String.class);

            idList.forEach(id ->
                tryWithdraw(JooqConfiguration.getTransferDao().fetchOneById(id)));
            LOGGER.info("Job execution ended");
            return new ResponsePojo("" + idList.size(), "Withdraws reattempted: " + idList.size());
        });
    }

    private static void tryWithdraw(Transfer transfer) {
        boolean withdrawResult = TransferService.processSenderWithdraw(transfer);
        if (!withdrawResult) {
            LOGGER.info("Could not withdraw amount successfully: account ID " + transfer.getSender() + ", amount "
                    + transfer.getAmount() + transfer.getSenderCurrency());
            Transfer updatedTransfer = new Transfer(transfer);
            updatedTransfer.setRetriesleft(updatedTransfer.getRetriesleft() - 1);
            JooqConfiguration.getTransferDao().update(transfer);
            TransferAttempt attempt = new TransferAttempt(transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                    UInteger.valueOf(TransferResult.ERROR_FAILED_WITHDRAW.code));
            JooqConfiguration.getTransferAttemptDao().insert(attempt);
        }

        LOGGER.info("Amount withdrawn successfully: account ID " + transfer.getSender() + ", amount " + transfer.getAmount()
                + transfer.getSenderCurrency());
        JooqConfiguration.getPendingWithdrawDao().deleteById(transfer.getId());
        RetryDepositJob.tryDeposit(transfer);
    }
}
