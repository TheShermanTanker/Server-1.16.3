package org.apache.logging.log4j.util;

import java.io.Writer;
import java.util.Objects;
import java.io.OutputStream;
import java.io.PrintWriter;

final class LowLevelLogUtil {
    private static PrintWriter writer;
    
    public static void logException(final Throwable exception) {
        exception.printStackTrace(LowLevelLogUtil.writer);
    }
    
    public static void logException(final String message, final Throwable exception) {
        if (message != null) {
            LowLevelLogUtil.writer.println(message);
        }
        logException(exception);
    }
    
    public static void setOutputStream(final OutputStream out) {
        LowLevelLogUtil.writer = new PrintWriter((OutputStream)Objects.requireNonNull(out), true);
    }
    
    public static void setWriter(final Writer writer) {
        LowLevelLogUtil.writer = new PrintWriter((Writer)Objects.requireNonNull(writer), true);
    }
    
    private LowLevelLogUtil() {
    }
    
    static {
        LowLevelLogUtil.writer = new PrintWriter((OutputStream)System.err, true);
    }
}
