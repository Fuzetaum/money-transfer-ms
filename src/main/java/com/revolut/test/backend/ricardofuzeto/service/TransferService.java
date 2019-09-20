package com.revolut.test.backend.ricardofuzeto.service;

import com.revolut.test.backend.ricardofuzeto.configuration.Environment;
import com.revolut.test.backend.ricardofuzeto.configuration.JooqConfiguration;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.PendingDeposit;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.PendingWithdraw;
import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import com.revolut.test.backend.ricardofuzeto.gateway.AccountWSGateway;
import com.revolut.test.backend.ricardofuzeto.model.ResponsePojo;
import com.revolut.test.backend.ricardofuzeto.model.TransferRequestPojo;
import com.revolut.test.backend.ricardofuzeto.model.TransferResult;
import com.revolut.test.backend.ricardofuzeto.utils.RequestUtils;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.revolut.test.backend.ricardofuzeto.database.tables.TransferAttempt.TRANSFER_ATTEMPT;

public class TransferService {
    private static Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    public static void registerTransfer() {
        JavalinApp.post("/transfer", ctx -> {
            TransferRequestPojo transferRequest = (TransferRequestPojo) RequestUtils.fromJson(ctx.body(), TransferRequestPojo.class);
            if (transferRequest.getSendercurrency().equals(transferRequest.getReceivercurrency())) {
                LOGGER.warn("transfer can't be processed: sender and receiver currencies are different");
                LOGGER.warn("transfers between accounts with different currencies is nt yet supported");
                ctx.status(404);
                return ResponsePojo.REQUEST_ERROR_DIFFERENT_CURRENCIES();
            }

            if (!AccountWSGateway.isAccountActive(transferRequest.getSender())) {
                LOGGER.warn("transfer can't be processed: sender ID does not match a valid account");
                ctx.status(404);
                return ResponsePojo.REQUEST_ERROR_SENDER_INVALID();
            }
            if (!AccountWSGateway.isAccountActive(transferRequest.getReceiver())) {
                LOGGER.warn("transfer can't be processed: receiver ID does not match a valid account");
                ctx.status(404);
                return ResponsePojo.REQUEST_ERROR_RECEIVER_INVALID();
            }

            Transfer transfer = processTransfer(transferRequest);
            if (transfer == null)
                ctx.status(202);
            else ctx.status(200);
            return transfer;
        });
    }

    public static boolean processSenderWithdraw(Transfer transfer) {
        boolean result = AccountWSGateway.withdrawAmountFromAccount(transfer.getSender(), transfer.getAmount());
        if (!result) {
            LOGGER.info("Transfer processing failed: ID " + transfer.getId());
            LOGGER.info("Reason: could not withdraw amount from sender account");
            Transfer newTransfer = new Transfer(transfer);
            newTransfer.setRetriesLeft(transfer.getRetriesLeft() - 1);
            JooqConfiguration.getTransferDao()
                    .update(newTransfer);
            JooqConfiguration.getDslContext()
                    .insertInto(TRANSFER_ATTEMPT)
                    .values(null, transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                            UInteger.valueOf(TransferResult.ERROR_FAILED_WITHDRAW.code))
                    .execute();
        }
        return result;
    }

    public static boolean processReceiverDeposit(Transfer transfer) {
        boolean result = AccountWSGateway.depositAmountToAccount(transfer.getSender(), transfer.getAmount());
        if (!result) {
            LOGGER.info("Transfer processing failed: ID " + transfer.getId());
            LOGGER.info("Reason: could not deposit amount to receiver account");
            Transfer newTransfer = new Transfer(transfer);
            newTransfer.setRetriesLeft(transfer.getRetriesLeft() - 1);
            JooqConfiguration.getTransferDao()
                    .update(newTransfer);
            JooqConfiguration.getDslContext()
                    .insertInto(TRANSFER_ATTEMPT)
                    .values(null, transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                            UInteger.valueOf(TransferResult.ERROR_FAILED_DEPOSIT.code))
                    .execute();
        }
        return result;
    }

    private static Transfer processTransfer(TransferRequestPojo transferRequest) {
        Transfer transfer = new Transfer(UUID.randomUUID().toString(), transferRequest.getSendercurrency(),
                transferRequest.getReceivercurrency(), transferRequest.getAmount(), transferRequest.getSendercurrency(),
                transferRequest.getReceivercurrency(), Integer.parseInt(Environment.get(Environment.TRANSFER_MAX_RETRIES)));
        JooqConfiguration.getTransferDao().insert(transfer);

        boolean hasWithdrawn = processSenderWithdraw(transfer);
        if (!hasWithdrawn) {
            PendingWithdraw pending = new PendingWithdraw(transfer.getId());
            JooqConfiguration.getPendingWithdrawDao().insert(pending);
            return null;
        }
        boolean hasDeposited = processReceiverDeposit(transfer);
        if (!hasDeposited) {
            PendingDeposit pending = new PendingDeposit(transfer.getId());
            JooqConfiguration.getPendingDepositDao().insert(pending);
            return null;
        }

        transfer.setRetriesLeft(0);
        JooqConfiguration.getTransferDao().update(transfer);
        JooqConfiguration.getDslContext()
                .insertInto(TRANSFER_ATTEMPT)
                .values(null, transfer.getId(), Timestamp.valueOf(LocalDateTime.now()),
                        UInteger.valueOf(TransferResult.SUCCESS.code))
                .execute();
        return transfer;
    }
}
