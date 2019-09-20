package com.revolut.test.backend.ricardofuzeto;

import com.revolut.test.backend.ricardofuzeto.configuration.Environment;
import com.revolut.test.backend.ricardofuzeto.job.RefundFailedWithdrawJob;
import com.revolut.test.backend.ricardofuzeto.job.RetryDepositJob;
import com.revolut.test.backend.ricardofuzeto.job.RetryWithdrawJob;
import com.revolut.test.backend.ricardofuzeto.service.JavalinApp;
import com.revolut.test.backend.ricardofuzeto.service.TransferService;

public class Main {
    public static void main(String[] args) {
        try {
            Environment.loadEnvironmentVariables();
            Environment.loadApplicationConfiguration();
            JavalinApp.initialize();
            TransferService.registerTransfer();
            RetryWithdrawJob.retryWithdraws();
            RetryDepositJob.retryDeposits();
            RefundFailedWithdrawJob.refundFailedWithdraws();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
