package com.revolut.test.backend.ricardofuzeto.utils;

public class HttpRequestPojoBuilder {
    private int status;
    private String response;

    public static HttpRequestPojoBuilder newBuilder() {
        return new HttpRequestPojoBuilder();
    }

    public HttpRequestPojoBuilder setStatus(int status) {
        this.status = status;
        return this;
    }

    public HttpRequestPojoBuilder setResponse(String response) {
        this.response = response;
        return this;
    }

    public HttpRequestPojo build() { return new HttpRequestPojo(status, response); }
}
