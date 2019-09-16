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

import static com.revolut.test.backend.ricardofuzeto.database.Tables.PENDING_DEPOSIT;
import static com.revolut.test.backend.ricardofuzeto.database.Tables.TRANSFER;

public class RetryDepositJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryDepositJob.class);

    public static void retryDeposits() {
        JavalinApp.get("/retry-deposits", ctx -> {
            LOGGER.info("Job execution started");
            List<String> idList = JooqConfiguration.getDslContext()
                    .select()
                    .from(PENDING_DEPOSIT)
                    .where(TRANSFER.RETRIESLEFT.greaterThan(0))
                    .fetch()
                    .getValues(PENDING_DEPOSIT.ID, String.class);

            idList.forEach(id ->
                tryDeposit(JooqConfiguration.getTransferDao().fetchOneById(id)));
            LOGGER.info("Job execution ended");
            return new ResponsePojo("" + idList.size(), "Deposits reattempted: " + idList.size());
        });
    }

    static void tryDeposit(Transfer transfer) {
        boolean result = TransferService.processReceiverDeposit(transfer);
        Transfer updatedTransfer = new Transfer(transfer);
        if (!result) {
            updatedTransfer.setRetriesleft(updatedTransfer.getRetriesleft() - 1);
            JooqConfiguration.getTransferDao().update(transfer);
            TransferAttempt attempt = new TransferAttempt(transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                    UInteger.valueOf(TransferResult.ERROR_FAILED_DEPOSIT.code));
            JooqConfiguration.getTransferAttemptDao().insert(attempt);
        }

        updatedTransfer.setRetriesleft(0);
        JooqConfiguration.getTransferDao().update(transfer);
        TransferAttempt attempt = new TransferAttempt(transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                UInteger.valueOf(TransferResult.SUCCESS.code));
        JooqConfiguration.getTransferAttemptDao().insert(attempt);
        JooqConfiguration.getPendingDepositDao().deleteById(transfer.getId());
    }
}
