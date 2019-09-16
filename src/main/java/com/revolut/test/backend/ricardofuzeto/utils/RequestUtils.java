package com.revolut.test.backend.ricardofuzeto.utils;

import com.revolut.test.backend.ricardofuzeto.configuration.gson.GsonConfiguration;

public abstract class RequestUtils {
    public static String toJson(Object object, Class<?> clazz) { return GsonConfiguration.GSON.toJson(object, clazz); }

    public static Object fromJson(String object, Class<?> clazz) { return GsonConfiguration.GSON.fromJson(object, clazz); }
}
