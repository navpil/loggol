package io.github.navpil.loggol.ourlib.jul;

public class SingleLineSimpleFormatter extends SimpleFormatterWithFormat {

    public SingleLineSimpleFormatter() {
        super("[JUL]%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }

}
