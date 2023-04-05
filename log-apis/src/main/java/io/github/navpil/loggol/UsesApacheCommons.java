package io.github.navpil.loggol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UsesApacheCommons {
    private static final Log LOG = LogFactory.getLog(UsesApacheCommons.class);

    public void doWithError() {
        LOG.error("I'm Apache Commons Logging error");
    }

    public void doWithWarn() {
        LOG.warn("I'm Apache Commons Logging warning");
    }

}
