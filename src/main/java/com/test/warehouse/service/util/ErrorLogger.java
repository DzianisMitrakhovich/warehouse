package com.test.warehouse.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLogger {

    public static void logError(Class clazz, Exception e, String errMsg) {
        Logger log = LoggerFactory.getLogger(clazz);
        log.error(String.format("%s. %s", errMsg, e.getMessage()), e);
    }
}
