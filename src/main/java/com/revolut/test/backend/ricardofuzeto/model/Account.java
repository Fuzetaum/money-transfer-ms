package com.revolut.test.backend.ricardofuzeto.model;

import org.jooq.types.UInteger;

public class Account {
    private String id;
    private String owner;
    private UInteger balance;
    private String currency;

    public Account(String id, String owner, UInteger balance, String currency) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public UInteger getBalance() {
        return balance;
    }

    public void setBalance(UInteger balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
