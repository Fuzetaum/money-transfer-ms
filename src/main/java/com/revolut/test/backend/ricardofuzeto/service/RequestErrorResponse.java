package com.revolut.test.backend.ricardofuzeto.service;

public class RequestErrorResponse {
    private String developerMessage;
    private String userMessage;

    public RequestErrorResponse(String developerMessage, String userMessage) {
        this.developerMessage = developerMessage;
        this.userMessage = userMessage;
    }

    public static RequestErrorResponse REQUEST_ERROR_SENDER_INVALID() {
        return new RequestErrorResponse("Sender ID was not validated by Account WS",
                "Sender account was not found or is not active anymore. Please, check sender's account data");
    }

    public static RequestErrorResponse REQUEST_ERROR_RECEIVER_INVALID() {
        return new RequestErrorResponse("Receiver ID was not validated by Account WS",
                "Receiver account was not found or is not active anymore. Please, check receiver's account data");
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
        return "RequestErrorResponse{" +
                "developerMessage='" + developerMessage + '\'' +
                ", userMessage='" + userMessage + '\'' +
                '}';
    }
}
