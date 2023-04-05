package io.github.navpil.loggol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsesSlf4j {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsesSlf4j.class);

    public void doWithError() {
        LOGGER.error("I'm SLF4j error");
    }

    public void doWithWarn() {
        LOGGER.warn("I'm SLF4j warning");
    }
}
