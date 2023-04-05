package io.github.navpil.loggol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsesLog4j2 {

    private static final Logger LOGGER = LogManager.getLogger(UsesLog4j2.class);

    public void doWithError() {
        LOGGER.error("I'm Log4j2 Error");
    }

    public void doWithWarn() {
        LOGGER.warn("I'm Log4j2 Warning");
    }

}
