package com.revolut.test.backend.ricardofuzeto.configuration;

import com.revolut.test.backend.ricardofuzeto.configuration.gson.GsonConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private static Map<String, String> ENVIRONMENT_VARIABLES = new HashMap<>();

    static String DATABASE_PASSWORD = "DATABASE_PASSWORD";
    static String DATABASE_URL = "DATABASE_URL";
    static String DATABASE_USERNAME = "DATABASE_USERNAME";
    public static String ACCOUNT_WS_URL = "ACCOUNT_WS_URL";
    public static String PORT = "PORT";
    public static String TRANSFER_MAX_RETRIES = "TRANSFER_MAX_RETRIES";

    public static String get(String varName) { return ENVIRONMENT_VARIABLES.get(varName); }

    public static void loadEnvironmentVariables() {
        String DATABASE_PORT = "DATABASE_PORT";
        String DATABASE_SCHEMA = "DATABASE_SCHEMA";
        String databaseUrl = "jdbc:mysql://" + System.getenv(DATABASE_URL) + ":" + System.getenv(DATABASE_PORT)
                + "/" + System.getenv(DATABASE_SCHEMA) + "?serverTimezone=UTC";

        ENVIRONMENT_VARIABLES.put(ACCOUNT_WS_URL, System.getenv(ACCOUNT_WS_URL));
        ENVIRONMENT_VARIABLES.put(DATABASE_PASSWORD, System.getenv(DATABASE_PASSWORD));
        ENVIRONMENT_VARIABLES.put(DATABASE_URL, databaseUrl);
        ENVIRONMENT_VARIABLES.put(DATABASE_USERNAME, System.getenv(DATABASE_USERNAME));
        ENVIRONMENT_VARIABLES.put(PORT, System.getenv(PORT));
        ENVIRONMENT_VARIABLES.put(TRANSFER_MAX_RETRIES, System.getenv(TRANSFER_MAX_RETRIES));
    }

    public static void loadApplicationConfiguration() {
        FlywayConfiguration.configure();
        JooqConfiguration.configure();
        GsonConfiguration.configure();
    }
}
