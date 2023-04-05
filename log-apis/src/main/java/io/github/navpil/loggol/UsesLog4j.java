package io.github.navpil.loggol;

import org.apache.log4j.Logger;

public class UsesLog4j {

    private static final Logger LOGGER = Logger.getLogger(UsesLog4j.class);

    public void doWithError() {
        LOGGER.error("I'm Log4j error");
    }

    public void doWithWarn() {
        LOGGER.warn("I'm Log4j warning");
    }

}
