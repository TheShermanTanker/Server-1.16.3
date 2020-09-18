package io.netty.util.internal;

import java.lang.reflect.InvocationTargetException;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.lang.reflect.Method;
import io.netty.util.internal.logging.InternalLogger;

final class CleanerJava9 implements Cleaner {
    private static final InternalLogger logger;
    private static final Method INVOKE_CLEANER;
    
    static boolean isSupported() {
        return CleanerJava9.INVOKE_CLEANER != null;
    }
    
    public void freeDirectBuffer(final ByteBuffer buffer) {
        try {
            CleanerJava9.INVOKE_CLEANER.invoke(PlatformDependent0.UNSAFE, new Object[] { buffer });
        }
        catch (Throwable cause) {
            PlatformDependent0.throwException(cause);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(CleanerJava9.class);
        Method method;
        Throwable error;
        if (PlatformDependent0.hasUnsafe()) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(1);
            Object maybeInvokeMethod;
            try {
                final Method m = PlatformDependent0.UNSAFE.getClass().getDeclaredMethod("invokeCleaner", new Class[] { ByteBuffer.class });
                m.invoke(PlatformDependent0.UNSAFE, new Object[] { buffer });
                maybeInvokeMethod = m;
            }
            catch (NoSuchMethodException e) {
                maybeInvokeMethod = e;
            }
            catch (InvocationTargetException e2) {
                maybeInvokeMethod = e2;
            }
            catch (IllegalAccessException e3) {
                maybeInvokeMethod = e3;
            }
            if (maybeInvokeMethod instanceof Throwable) {
                method = null;
                error = (Throwable)maybeInvokeMethod;
            }
            else {
                method = (Method)maybeInvokeMethod;
                error = null;
            }
        }
        else {
            method = null;
            error = (Throwable)new UnsupportedOperationException("sun.misc.Unsafe unavailable");
        }
        if (error == null) {
            CleanerJava9.logger.debug("java.nio.ByteBuffer.cleaner(): available");
        }
        else {
            CleanerJava9.logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
        }
        INVOKE_CLEANER = method;
    }
}
