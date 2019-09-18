package com.revolut.test.backend.ricardofuzeto.model;

public class ResponsePojo {
    private String developerMessage;
    private String userMessage;

    public ResponsePojo(String developerMessage, String userMessage) {
        this.developerMessage = developerMessage;
        this.userMessage = userMessage;
    }

    public static ResponsePojo REQUEST_ERROR_SENDER_INVALID() {
        return new ResponsePojo("Sender ID was not validated by Account WS",
                "Sender account was not found or is not active anymore. Please, check sender's account data");
    }

    public static ResponsePojo REQUEST_ERROR_RECEIVER_INVALID() {
        return new ResponsePojo("Receiver ID was not validated by Account WS",
                "Receiver account was not found or is not active anymore. Please, check receiver's account data");
    }

    public static ResponsePojo REQUEST_ERROR_DIFFERENT_CURRENCIES() {
        return new ResponsePojo("Sender and receiver accounts' currencies do not match",
                "Sender and receiver accounts' currencies are not the same. This support is not yet provided");
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    @Override
    public String toString() {
        return "ResponsePojo{" +
                "developerMessage='" + developerMessage + '\'' +
                ", userMessage='" + userMessage + '\'' +
                '}';
    }
}
