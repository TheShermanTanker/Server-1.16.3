package io.netty.util;

public class IllegalReferenceCountException extends IllegalStateException {
    private static final long serialVersionUID = -2507492394288153468L;
    
    public IllegalReferenceCountException() {
    }
    
    public IllegalReferenceCountException(final int refCnt) {
        this(new StringBuilder().append("refCnt: ").append(refCnt).toString());
    }
    
    public IllegalReferenceCountException(final int refCnt, final int increment) {
        this(new StringBuilder().append("refCnt: ").append(refCnt).append(", ").append((increment > 0) ? new StringBuilder().append("increment: ").append(increment).toString() : new StringBuilder().append("decrement: ").append(-increment).toString()).toString());
    }
    
    public IllegalReferenceCountException(final String message) {
        super(message);
    }
    
    public IllegalReferenceCountException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public IllegalReferenceCountException(final Throwable cause) {
        super(cause);
    }
}
