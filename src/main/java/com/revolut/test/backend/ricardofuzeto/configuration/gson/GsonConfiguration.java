package com.revolut.test.backend.ricardofuzeto.configuration.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jooq.types.UInteger;

public class GsonConfiguration {
    public static Gson GSON;

    public static void configure() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UInteger.class, new UIntegerTypeAdapter());
        builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        GSON = builder.create();
    }
}
