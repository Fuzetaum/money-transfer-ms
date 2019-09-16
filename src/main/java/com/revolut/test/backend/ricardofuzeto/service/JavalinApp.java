package com.revolut.test.backend.ricardofuzeto.service;

import com.revolut.test.backend.ricardofuzeto.configuration.Environment;
import com.revolut.test.backend.ricardofuzeto.utils.RequestUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavalinApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavalinApp.class);
    private static Javalin APP = null;

    public static void initialize() {
        if (APP == null) {
            int port = Integer.parseInt(Environment.get(Environment.PORT));
            APP = Javalin.create().start(port);
        }
    }

    public static void get(String path, JavalinServiceWrapper lambda) {
        LOGGER.info("endpoint mapped: GET " + path);
        APP.get(path, ctx -> {
            LOGGER.info("request received: GET " + ctx.req.getPathInfo());
            Object returnValue = lambda.handle(ctx);
            ctx.result(RequestUtils.toJson(returnValue, returnValue.getClass()));
        });
    }
    static void post(String path, JavalinServiceWrapper lambda) {
        LOGGER.info("endpoint mapped: POST " + path);
        APP.post(path, ctx -> {
            LOGGER.info("request received: POST " + ctx.req.getPathInfo());
            Object returnValue = lambda.handle(ctx);
            ctx.result(RequestUtils.toJson(returnValue, returnValue.getClass()));
        });
    }
}
