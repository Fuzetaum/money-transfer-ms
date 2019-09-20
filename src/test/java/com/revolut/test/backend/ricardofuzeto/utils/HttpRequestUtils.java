package com.revolut.test.backend.ricardofuzeto.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRequestUtils {
    public static HttpRequestPojo sendPost(String url, String jsonBody) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", Integer.toString(jsonBody.length()));
        connection.setDoOutput(true);
        connection.getOutputStream().write(jsonBody.getBytes(StandardCharsets.UTF_8));
        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return HttpRequestPojoBuilder.newBuilder()
                .setStatus(status)
                .setResponse(content.toString())
                .build();
    }
}
