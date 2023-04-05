package io.github.navpil.loggol.ourlib.jul;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class SimpleFormatterWithFormat extends Formatter {

    private String format;

    public static Formatter doubleLine() {
        return new SimpleFormatterWithFormat("%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n");
    }

    public static Formatter singleLine() {
        return new SimpleFormatterWithFormat("%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }

    public SimpleFormatterWithFormat(String format) {
        this.format = format;
    }

    static String getLoggingProperty(String name) {
        return LogManager.getLogManager().getProperty(name);
    }

    public String format(LogRecord record) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(record.getInstant(), ZoneId.systemDefault());
        String source;
        if (record.getSourceClassName() != null) {
            source = record.getSourceClassName();
            if (record.getSourceMethodName() != null) {
                source = source + " " + record.getSourceMethodName();
            }
        } else {
            source = record.getLoggerName();
        }

        String message = this.formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }

        return String.format(this.format, zdt, source, record.getLoggerName(), record.getLevel().getLocalizedName(), message, throwable);
    }}
