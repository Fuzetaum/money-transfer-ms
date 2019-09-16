package com.revolut.test.backend.ricardofuzeto.service;

import io.javalin.http.Context;

public interface JavalinServiceWrapper {
    Object handle(Context ctx);
}
