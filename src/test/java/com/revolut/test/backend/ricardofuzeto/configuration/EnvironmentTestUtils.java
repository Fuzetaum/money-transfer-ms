package com.revolut.test.backend.ricardofuzeto.configuration;

import com.revolut.test.backend.ricardofuzeto.job.RefundFailedWithdrawJob;
import com.revolut.test.backend.ricardofuzeto.job.RetryDepositJob;
import com.revolut.test.backend.ricardofuzeto.job.RetryWithdrawJob;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import com.revolut.test.backend.ricardofuzeto.service.TransferService;

public class EnvironmentTestUtils {
    private static boolean IS_ENVIRONMENT_LOADED = false;

    public static void configure() {
        if (!IS_ENVIRONMENT_LOADED) {
            setDatabaseUrl();
            setDatabaseUsername();
            setDatabasePassword();
            setAccountWsUrl();
            setPort();
            setMaxRetries();
            Environment.loadApplicationConfiguration();
            JavalinApp.initialize();
            TransferService.registerTransfer("/transfer");
            RetryWithdrawJob.retryWithdraws();
            RetryDepositJob.retryDeposits();
            RefundFailedWithdrawJob.refundFailedWithdraws();
            IS_ENVIRONMENT_LOADED = true;
        }
    }

    private static void setDatabaseUrl() { Environment.setEnvironmentVariable(Environment.DATABASE_URL, "jdbc:h2:mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1"); }
    private static void setDatabaseUsername() { Environment.setEnvironmentVariable(Environment.DATABASE_USERNAME, ""); }
    private static void setDatabasePassword() { Environment.setEnvironmentVariable(Environment.DATABASE_PASSWORD, ""); }
    private static void setAccountWsUrl() { Environment.setEnvironmentVariable(Environment.ACCOUNT_WS_URL, "http://localhost:9090"); }
    private static void setPort() { Environment.setEnvironmentVariable(Environment.PORT, "8090"); }
    private static void setMaxRetries() { Environment.setEnvironmentVariable(Environment.TRANSFER_MAX_RETRIES, "10"); }
}
