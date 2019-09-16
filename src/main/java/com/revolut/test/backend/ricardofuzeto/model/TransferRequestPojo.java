package com.revolut.test.backend.ricardofuzeto.model;

import org.jooq.types.UInteger;

public class TransferRequestPojo {
    private String sender;
    private String receiver;
    private UInteger amount;
    private String sendercurrency;
    private String receivercurrency;

    public TransferRequestPojo(String sender, String receiver, UInteger amount, String sendercurrency, String receivercurrency) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.sendercurrency = sendercurrency;
        this.receivercurrency = receivercurrency;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public UInteger getAmount() {
        return amount;
    }

    public void setAmount(UInteger amount) {
        this.amount = amount;
    }

    public String getSendercurrency() {
        return sendercurrency;
    }

    public void setSendercurrency(String sendercurrency) {
        this.sendercurrency = sendercurrency;
    }

    public String getReceivercurrency() {
        return receivercurrency;
    }

    public void setReceivercurrency(String receivercurrency) {
        this.receivercurrency = receivercurrency;
    }

    @Override
    public String toString() {
        return "TransferRequestPojo{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", amount=" + amount +
                ", sendercurrency='" + sendercurrency + '\'' +
                ", receivercurrency='" + receivercurrency + '\'' +
                '}';
    }
}
