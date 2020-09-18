package io.netty.channel.kqueue;

import io.netty.channel.unix.FileDescriptor;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;

public final class KQueue {
    private static final Throwable UNAVAILABILITY_CAUSE;
    
    public static boolean isAvailable() {
        return KQueue.UNAVAILABILITY_CAUSE == null;
    }
    
    public static void ensureAvailability() {
        if (KQueue.UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(KQueue.UNAVAILABILITY_CAUSE);
        }
    }
    
    public static Throwable unavailabilityCause() {
        return KQueue.UNAVAILABILITY_CAUSE;
    }
    
    private KQueue() {
    }
    
    static {
        Throwable cause = null;
        if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
            cause = (Throwable)new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
        }
        else {
            FileDescriptor kqueueFd = null;
            try {
                kqueueFd = Native.newKQueue();
            }
            catch (Throwable t) {
                cause = t;
            }
            finally {
                if (kqueueFd != null) {
                    try {
                        kqueueFd.close();
                    }
                    catch (Exception ex) {}
                }
            }
        }
        if (cause != null) {
            UNAVAILABILITY_CAUSE = cause;
        }
        else {
            UNAVAILABILITY_CAUSE = (Throwable)(PlatformDependent.hasUnsafe() ? null : new IllegalStateException("sun.misc.Unsafe not available", PlatformDependent.getUnsafeUnavailabilityCause()));
        }
    }
}
