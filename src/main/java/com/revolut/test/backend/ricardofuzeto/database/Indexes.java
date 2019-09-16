/*
 * This file is generated by jOOQ.
 */
package com.revolut.test.backend.ricardofuzeto.database;


import com.revolut.test.backend.ricardofuzeto.database.tables.FlywaySchemaHistory;
import com.revolut.test.backend.ricardofuzeto.database.tables.PendingDeposit;
import com.revolut.test.backend.ricardofuzeto.database.tables.PendingWithdraw;
import com.revolut.test.backend.ricardofuzeto.database.tables.Transfer;
import com.revolut.test.backend.ricardofuzeto.database.tables.TransferAttempt;

import javax.annotation.processing.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code></code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index FLYWAY_SCHEMA_HISTORY_FLYWAY_SCHEMA_HISTORY_S_IDX = Indexes0.FLYWAY_SCHEMA_HISTORY_FLYWAY_SCHEMA_HISTORY_S_IDX;
    public static final Index FLYWAY_SCHEMA_HISTORY_PRIMARY = Indexes0.FLYWAY_SCHEMA_HISTORY_PRIMARY;
    public static final Index PENDING_DEPOSIT_PRIMARY = Indexes0.PENDING_DEPOSIT_PRIMARY;
    public static final Index PENDING_WITHDRAW_PRIMARY = Indexes0.PENDING_WITHDRAW_PRIMARY;
    public static final Index TRANSFER_PRIMARY = Indexes0.TRANSFER_PRIMARY;
    public static final Index TRANSFER_ATTEMPT_PRIMARY = Indexes0.TRANSFER_ATTEMPT_PRIMARY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index FLYWAY_SCHEMA_HISTORY_FLYWAY_SCHEMA_HISTORY_S_IDX = Internal.createIndex("flyway_schema_history_s_idx", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS }, false);
        public static Index FLYWAY_SCHEMA_HISTORY_PRIMARY = Internal.createIndex("PRIMARY", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
        public static Index PENDING_DEPOSIT_PRIMARY = Internal.createIndex("PRIMARY", PendingDeposit.PENDING_DEPOSIT, new OrderField[] { PendingDeposit.PENDING_DEPOSIT.ID }, true);
        public static Index PENDING_WITHDRAW_PRIMARY = Internal.createIndex("PRIMARY", PendingWithdraw.PENDING_WITHDRAW, new OrderField[] { PendingWithdraw.PENDING_WITHDRAW.ID }, true);
        public static Index TRANSFER_PRIMARY = Internal.createIndex("PRIMARY", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.ID }, true);
        public static Index TRANSFER_ATTEMPT_PRIMARY = Internal.createIndex("PRIMARY", TransferAttempt.TRANSFER_ATTEMPT, new OrderField[] { TransferAttempt.TRANSFER_ATTEMPT.ID }, true);
    }
}
