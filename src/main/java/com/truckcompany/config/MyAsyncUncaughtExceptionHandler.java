package com.truckcompany.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Created by Vladimir on 28.11.2016.
 */
public class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    public static final Logger LOG = LoggerFactory.getLogger(MyAsyncUncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        LOG.debug("Attention");

    }
}
