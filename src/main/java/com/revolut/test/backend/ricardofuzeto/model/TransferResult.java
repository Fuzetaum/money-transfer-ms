package com.revolut.test.backend.ricardofuzeto.model;

public enum TransferResult {
    SUCCESS (0),
    ERROR_FAILED_WITHDRAW (1),
    ERROR_FAILED_DEPOSIT (2);

    public Integer code;

    TransferResult(Integer code) {
        this.code = code;
    }
}
