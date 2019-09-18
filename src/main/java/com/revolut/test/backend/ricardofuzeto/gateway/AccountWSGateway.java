package com.revolut.test.backend.ricardofuzeto.gateway;

import com.revolut.test.backend.ricardofuzeto.configuration.Environment;
import com.revolut.test.backend.ricardofuzeto.model.AccountPojo;
import com.revolut.test.backend.ricardofuzeto.model.ResponsePojo;
import com.revolut.test.backend.ricardofuzeto.utils.RequestUtils;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountWSGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountWSGateway.class);
    private static final String ACCOUNT_WS_URL = Environment.get(Environment.ACCOUNT_WS_URL);
    private static final String ACCOUNT_WS_VERIFY_URI = "/account/";
    private static final String ACCOUNT_WS_DEPOSIT_URI = "/deposit";
    private static final String ACCOUNT_WS_WITHDRAW_URI = "/withdraw";

    public static boolean isAccountActive(String accountId) {
        boolean result = false;
        try {
            URL url = new URL(ACCOUNT_WS_URL + ACCOUNT_WS_VERIFY_URI + accountId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);

            int status = connection.getResponseCode();
            LOGGER.info("Request: verify account ID validity");
            LOGGER.info("Response status: " + status);
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                AccountPojo account = (AccountPojo) RequestUtils.fromJson(content.toString(), AccountPojo.class);
                LOGGER.info("Account data: " + account.toString());
                result = true;
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Error while verifying account: ID " + accountId);
            LOGGER.error("Reason: malformed URL - " + ACCOUNT_WS_URL);
        } catch (IOException e) {
            LOGGER.error("Error while verifying account: ID " + accountId);
            LOGGER.error("Reason: could not connect to Account WS");
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    public static boolean depositAmountToAccount(String accountId, UInteger amount) {
        boolean result = false;
        try {
            URL url = new URL(ACCOUNT_WS_URL + ACCOUNT_WS_VERIFY_URI + accountId + ACCOUNT_WS_DEPOSIT_URI
                    + "?amount=" + amount.intValue());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);

            int status = connection.getResponseCode();
            LOGGER.info("Request: deposit to account");
            LOGGER.info("Response status: " + status);
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                ResponsePojo response = (ResponsePojo) RequestUtils.fromJson(content.toString(), ResponsePojo.class);
                LOGGER.info("Response: " + response.toString());
                result = true;
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Error while depositing to account: ID " + accountId);
            LOGGER.error("Reason: malformed URL - " + ACCOUNT_WS_URL);
        } catch (IOException e) {
            LOGGER.error("Error while depositing to account: ID " + accountId);
            LOGGER.error("Reason: could not connect to Account WS");
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    public static boolean withdrawAmountFromAccount(String accountId, UInteger amount) {
        boolean result = false;
        try {
            URL url = new URL(ACCOUNT_WS_URL + ACCOUNT_WS_VERIFY_URI + accountId + ACCOUNT_WS_WITHDRAW_URI
            + "?amount=" + amount.intValue());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);

            int status = connection.getResponseCode();
            LOGGER.info("Request: withdraw from account");
            LOGGER.info("Response status: " + status);
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                ResponsePojo response = (ResponsePojo) RequestUtils.fromJson(content.toString(),
                        ResponsePojo.class);
                LOGGER.info("Response: " + response.toString());
                result = true;
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Error while withdrawing from account: ID " + accountId);
            LOGGER.error("Reason: malformed URL - " + ACCOUNT_WS_URL);
        } catch (IOException e) {
            LOGGER.error("Error while withdrawing from account: ID " + accountId);
            LOGGER.error("Reason: could not connect to Account WS");
            LOGGER.error(e.getMessage());
        }
        return result;
    }
}
