package com.revolut.test.backend.ricardofuzeto.configuration;

import org.flywaydb.core.Flyway;

public class FlywayConfiguration {
    public static void configure() {
        Flyway.configure()
            .dataSource(Environment.get(Environment.DATABASE_URL), Environment.get(Environment.DATABASE_USERNAME),
                    Environment.get(Environment.DATABASE_PASSWORD))
            .load()
            .migrate();
    }
}
