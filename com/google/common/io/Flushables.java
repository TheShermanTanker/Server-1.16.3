package com.google.common.io;

import java.io.IOException;
import java.util.logging.Level;
import java.io.Flushable;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class Flushables {
    private static final Logger logger;
    
    private Flushables() {
    }
    
    public static void flush(final Flushable flushable, final boolean swallowIOException) throws IOException {
        try {
            flushable.flush();
        }
        catch (IOException e) {
            if (!swallowIOException) {
                throw e;
            }
            Flushables.logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", (Throwable)e);
        }
    }
    
    public static void flushQuietly(final Flushable flushable) {
        try {
            flush(flushable, true);
        }
        catch (IOException e) {
            Flushables.logger.log(Level.SEVERE, "IOException should not have been thrown.", (Throwable)e);
        }
    }
    
    static {
        logger = Logger.getLogger(Flushables.class.getName());
    }
}
