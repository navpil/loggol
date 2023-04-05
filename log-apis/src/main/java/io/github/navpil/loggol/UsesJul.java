package io.github.navpil.loggol;

import java.util.logging.Logger;

public class UsesJul {
    private static final Logger LOGGER = Logger.getLogger(UsesJul.class.getName());

    public void doWithError() {
        LOGGER.severe("I'm JUL severe");
    }

    public void doWithWarn() {
        LOGGER.warning("I'm JUL warning");
    }

}
