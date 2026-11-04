package org.example.lab10.task2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class LogFormatter extends Formatter {
    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

    private final boolean withStackTrace;

    public LogFormatter(boolean withStackTrace) {
        this.withStackTrace = withStackTrace;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder line = new StringBuilder();
        line.append(TIME.format(record.getInstant()))
                .append(' ')
                .append(String.format("%-7s", record.getLevel().getName()))
                .append(" [").append(shortName(record.getLoggerName())).append("] ")
                .append(formatMessage(record))
                .append(System.lineSeparator());

        Throwable thrown = record.getThrown();
        if (thrown != null) {
            // У файл записуємо повний стек помилки, а в консоль — лише коротку причину
            if (withStackTrace) {
                StringWriter trace = new StringWriter();
                thrown.printStackTrace(new PrintWriter(trace));
                line.append(trace);
            } else {
                line.append("    причина: ").append(thrown).append(System.lineSeparator());
            }
        }
        return line.toString();
    }

    private static String shortName(String loggerName) {
        if (loggerName == null) {
            return "root";
        }
        return loggerName.substring(loggerName.lastIndexOf('.') + 1);
    }
}
