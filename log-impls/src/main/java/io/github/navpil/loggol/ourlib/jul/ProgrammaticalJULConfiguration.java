package io.github.navpil.loggol.ourlib.jul;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammaticalJULConfiguration {

    public ProgrammaticalJULConfiguration() throws IOException {
        //Some configuration logic goes here
        //Presumably we can have several such configurations and we can switch between them without recompiling
        Logger logger = Logger.getLogger("io.github.navpil");
        logger.setLevel(Level.FINEST);

        Handler fileHandler = new FileHandler("target/jul-trace.%u.%g.log");
        fileHandler.setFormatter(SimpleFormatterWithFormat.singleLine());
        fileHandler.setLevel(Level.ALL);

        logger.addHandler(fileHandler);
    }
}
