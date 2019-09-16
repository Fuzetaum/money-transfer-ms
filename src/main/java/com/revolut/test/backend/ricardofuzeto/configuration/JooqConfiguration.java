package com.revolut.test.backend.ricardofuzeto.configuration;

import com.revolut.test.backend.ricardofuzeto.database.tables.daos.PendingDepositDao;
import com.revolut.test.backend.ricardofuzeto.database.tables.daos.PendingWithdrawDao;
import com.revolut.test.backend.ricardofuzeto.database.tables.daos.TransferAttemptDao;
import com.revolut.test.backend.ricardofuzeto.database.tables.daos.TransferDao;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JooqConfiguration {
    private static TransferDao transferDao;
    private static TransferAttemptDao transferAttemptDao;
    private static PendingWithdrawDao pendingWithdrawDao;
    private static PendingDepositDao pendingDepositDao;
    private static DSLContext dslContext;

    public static TransferDao getTransferDao() { return transferDao; }
    public static TransferAttemptDao getTransferAttemptDao() { return transferAttemptDao; }
    public static PendingWithdrawDao getPendingWithdrawDao() { return pendingWithdrawDao; }
    public static PendingDepositDao getPendingDepositDao() { return pendingDepositDao; }

    public static DSLContext getDslContext() { return dslContext; }

    static void configure() {
        try {
            Connection connection = DriverManager.getConnection(Environment.get(Environment.DATABASE_URL),
                    Environment.get(Environment.DATABASE_USERNAME), Environment.get(Environment.DATABASE_PASSWORD));
            createDaos(connection);
            dslContext = DSL.using(connection, SQLDialect.MYSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDaos(Connection connection) {
        Configuration config = new DefaultConfiguration().set(connection).set(SQLDialect.MYSQL);
        transferDao = new TransferDao(config);
        transferAttemptDao = new TransferAttemptDao(config);
        pendingWithdrawDao = new PendingWithdrawDao(config);
        pendingDepositDao = new PendingDepositDao(config);
    }
}
