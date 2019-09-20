package com.revolut.test.backend.ricardofuzeto.utils;

public class HttpRequestPojo {
    private int status;
    private String response;

    HttpRequestPojo(int status, String response) {
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
