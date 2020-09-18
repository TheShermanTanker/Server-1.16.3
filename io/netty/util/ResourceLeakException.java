package io.netty.util;

import java.util.Arrays;

@Deprecated
public class ResourceLeakException extends RuntimeException {
    private static final long serialVersionUID = 7186453858343358280L;
    private final StackTraceElement[] cachedStackTrace;
    
    public ResourceLeakException() {
        this.cachedStackTrace = this.getStackTrace();
    }
    
    public ResourceLeakException(final String message) {
        super(message);
        this.cachedStackTrace = this.getStackTrace();
    }
    
    public ResourceLeakException(final String message, final Throwable cause) {
        super(message, cause);
        this.cachedStackTrace = this.getStackTrace();
    }
    
    public ResourceLeakException(final Throwable cause) {
        super(cause);
        this.cachedStackTrace = this.getStackTrace();
    }
    
    public int hashCode() {
        final StackTraceElement[] trace = this.cachedStackTrace;
        int hashCode = 0;
        for (final StackTraceElement e : trace) {
            hashCode = hashCode * 31 + e.hashCode();
        }
        return hashCode;
    }
    
    public boolean equals(final Object o) {
        return o instanceof ResourceLeakException && (o == this || Arrays.equals((Object[])this.cachedStackTrace, (Object[])((ResourceLeakException)o).cachedStackTrace));
    }
}
