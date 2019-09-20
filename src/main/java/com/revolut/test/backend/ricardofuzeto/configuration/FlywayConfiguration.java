package com.revolut.test.backend.ricardofuzeto.configuration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.exception.FlywaySqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlywayConfiguration {
    private static Logger LOGGER = LoggerFactory.getLogger(FlywayConfiguration.class);

    public static void configure() throws InterruptedException {
        int retriesLeft = 10;
        while (true) {
            try {
                Flyway.configure()
                        .dataSource(Environment.get(Environment.DATABASE_URL), Environment.get(Environment.DATABASE_USERNAME),
                                Environment.get(Environment.DATABASE_PASSWORD))
                        .load()
                        .migrate();
                break;
            } catch (FlywaySqlException fse) {
                if (retriesLeft > 0) {
                    LOGGER.warn("Flyway failed to connect to MySQL. Waiting for 2 seconds before retrying...");
                    Thread.sleep(2000);
                    retriesLeft--;
                } else {
                    LOGGER.error("Could not connect to MySQL");
                    LOGGER.error(fse.getMessage());
                }
            }
        }
    }
}
