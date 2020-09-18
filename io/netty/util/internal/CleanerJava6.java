package io.netty.util.internal;

import java.lang.reflect.Field;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import io.netty.util.internal.logging.InternalLogger;
import java.lang.reflect.Method;

final class CleanerJava6 implements Cleaner {
    private static final long CLEANER_FIELD_OFFSET;
    private static final Method CLEAN_METHOD;
    private static final InternalLogger logger;
    
    static boolean isSupported() {
        return CleanerJava6.CLEANER_FIELD_OFFSET != -1L;
    }
    
    public void freeDirectBuffer(final ByteBuffer buffer) {
        if (!buffer.isDirect()) {
            return;
        }
        try {
            final Object cleaner = PlatformDependent0.getObject(buffer, CleanerJava6.CLEANER_FIELD_OFFSET);
            if (cleaner != null) {
                CleanerJava6.CLEAN_METHOD.invoke(cleaner, new Object[0]);
            }
        }
        catch (Throwable cause) {
            PlatformDependent0.throwException(cause);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(CleanerJava6.class);
        long fieldOffset = -1L;
        Method clean = null;
        Throwable error = null;
        if (PlatformDependent0.hasUnsafe()) {
            final ByteBuffer direct = ByteBuffer.allocateDirect(1);
            try {
                final Field cleanerField = direct.getClass().getDeclaredField("cleaner");
                fieldOffset = PlatformDependent0.objectFieldOffset(cleanerField);
                final Object cleaner = PlatformDependent0.getObject(direct, fieldOffset);
                clean = cleaner.getClass().getDeclaredMethod("clean", new Class[0]);
                clean.invoke(cleaner, new Object[0]);
            }
            catch (Throwable t) {
                fieldOffset = -1L;
                clean = null;
                error = t;
            }
        }
        else {
            error = (Throwable)new UnsupportedOperationException("sun.misc.Unsafe unavailable");
        }
        if (error == null) {
            CleanerJava6.logger.debug("java.nio.ByteBuffer.cleaner(): available");
        }
        else {
            CleanerJava6.logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
        }
        CLEANER_FIELD_OFFSET = fieldOffset;
        CLEAN_METHOD = clean;
    }
}
