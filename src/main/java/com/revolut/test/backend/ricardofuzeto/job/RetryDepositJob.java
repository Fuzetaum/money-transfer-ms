package com.revolut.test.backend.ricardofuzeto.job;

import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import com.revolut.test.backend.ricardofuzeto.model.JobRequestPojo;
import com.revolut.test.backend.ricardofuzeto.model.ResponsePojo;
import com.revolut.test.backend.ricardofuzeto.model.TransferResult;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import com.revolut.test.backend.ricardofuzeto.service.TransferService;
import com.revolut.test.backend.ricardofuzeto.utils.RequestUtils;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.revolut.test.backend.ricardofuzeto.database.Tables.PENDING_DEPOSIT;
import static com.revolut.test.backend.ricardofuzeto.database.Tables.TRANSFER;
import static com.revolut.test.backend.ricardofuzeto.database.tables.TransferAttempt.TRANSFER_ATTEMPT;

public class RetryDepositJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryDepositJob.class);

    public static void retryDeposits() {
        JavalinApp.post("/retry-deposits", ctx -> {
            JobRequestPojo requestPojo = (JobRequestPojo) RequestUtils.fromJson(ctx.body(), JobRequestPojo.class);
            LOGGER.info("Job execution started: replica #"+ requestPojo.getInstance());
            Integer perReplicaTransferCount = Math.round(JooqConfiguration.getDslContext()
                    .selectCount()
                    .from(TRANSFER)
                    .where(TRANSFER.RETRIES_LEFT.greaterThan(0))
                    .fetchOne(0, Integer.class) / requestPojo.getReplicas().floatValue());
            List<String> idList = JooqConfiguration.getDslContext()
                    .select(PENDING_DEPOSIT.ID)
                    .from(PENDING_DEPOSIT)
                    .join(TRANSFER).on(PENDING_DEPOSIT.ID.eq(TRANSFER.ID))
                    .where(TRANSFER.RETRIES_LEFT.greaterThan(0))
                    .limit(perReplicaTransferCount)
                    .offset(perReplicaTransferCount * (requestPojo.getInstance() - 1))
                    .fetch()
                    .getValues(PENDING_DEPOSIT.ID, String.class);

            idList.forEach(id ->
                tryDeposit(JooqConfiguration.getTransferDao().fetchOneById(id)));
            LOGGER.info("Job execution ended");
            return new ResponsePojo("" + idList.size(), "Deposits reattempted: " + idList.size());
        });
    }

    private static void tryDeposit(Transfer transfer) {
        boolean result = TransferService.processReceiverDeposit(transfer);
        Transfer updatedTransfer = new Transfer(transfer);
        if (!result) {
            updatedTransfer.setRetriesLeft(updatedTransfer.getRetriesLeft() - 1);
            JooqConfiguration.getTransferDao().update(transfer);
            JooqConfiguration.getDslContext()
                    .insertInto(TRANSFER_ATTEMPT)
                    .values(null, transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                            UInteger.valueOf(TransferResult.ERROR_FAILED_DEPOSIT.code))
                    .execute();
            return;
        }

        updatedTransfer.setRetriesLeft(0);
        JooqConfiguration.getTransferDao().update(transfer);
        JooqConfiguration.getDslContext()
                .insertInto(TRANSFER_ATTEMPT)
                .values(null, transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                        UInteger.valueOf(TransferResult.SUCCESS.code))
                .execute();
        JooqConfiguration.getPendingDepositDao().deleteById(transfer.getId());
    }
}
